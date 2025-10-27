package com.upskilling.experiment.service.impl;


import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.upskilling.experiment.entity.Task;
import com.upskilling.experiment.entity.User;
import com.upskilling.experiment.service.NotificationService;
import org.springframework.mail.javamail.JavaMailSender;

/**
 * Service implementation for email notification functionality.
 * Handles sending email notifications for task assignments, status changes, and other events.
 * Emails are sent asynchronously using Spring's @Async annotation to avoid blocking operations.
 * Uses JavaMailSender for SMTP email delivery.
 * 
 * @author Task Management System
 * @version 1.0
 * @since 1.0
 */
@Slf4j
@Service
public class NotificationServiceImpl implements NotificationService {

    /** Mail sender for sending email notifications */
    @Autowired
    private JavaMailSender mailSender;

    /**
     * Notify a user when they are assigned to a new task
     * Sends an email with task details to the assignee
     * Email will be sent FROM the task creator's email address
     * 
     * @param assignee The user being assigned to the task
     * @param task The task being assigned
     */
    public void notifyTaskAssignment(User assignee, Task task) {
        String subject = String.format("[NEW TASK] Assigned to Task #%d: '%s'", task.getId(), task.getTitle());
        String text = String.format("You have been assigned to task: '%s'.\n\nCreated by: %s (%s)",
                task.getTitle(), task.getCreator().getUsername(), task.getCreator().getEmail());

        // Get the creator's email to use as sender
        String senderEmail = task.getCreator().getEmail();
        
        // Call the reusable, asynchronous sender with creator's email as sender
        sendEmail(assignee.getEmail(), subject, text, senderEmail);
        log.info("NOTIFICATION TRIGGERED: Task '{}' assigned to user {} ({}). Sent from {}.",
                task.getTitle(), assignee.getUsername(), assignee.getEmail(), senderEmail);
    }

    @Async // Feature: Email notification (Spring Mail)
    public void sendNotification(Task task, String eventType, User performingUser) {
        User recipient = task.getAssignee();
        if (recipient == null || recipient.getEmail() == null) return;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("no-reply@taskmanager.com");
        message.setTo(recipient.getEmail());

        String subject = "";
        String text = "";

        if ("STATUS_CHANGE".equals(eventType)) {
            subject = String.format("[UPDATE] Task #%d Status Changed", task.getId());
            text = String.format("Task '%s' status was updated to %s by %s.",
                    task.getTitle(), task.getStatus(), performingUser.getUsername());
        } else if ("ASSIGNMENT".equals(eventType)) { // Feature: Notification when assigned
            subject = String.format("[NEW TASK] Assigned to Task #%d: '%s'", task.getId(), task.getTitle());
            text = String.format("You have been assigned to task: '%s'.", task.getTitle());
        }

        if (!subject.isEmpty()) {
           sendEmail(recipient.getEmail(), subject, text);
        }
    }

    @Override
    public void notifyTaskStatusChange(Task task, String oldStatus, String newStatus) {
        User recipient = task.getAssignee();
        if (recipient == null || recipient.getEmail() == null) return;

        String subject = String.format("[UPDATE] Task #%d Status Changed", task.getId());
        String text = String.format("Task '%s' status was updated from %s to %s.\n\nUpdated by: %s (%s)",
                task.getTitle(), oldStatus, newStatus, 
                task.getCreator().getUsername(), task.getCreator().getEmail());

        // Get the creator's email to use as sender
        String senderEmail = task.getCreator().getEmail();

        // Call the reusable, asynchronous sender with creator's email as sender
        sendEmail(recipient.getEmail(), subject, text, senderEmail);
        log.info("NOTIFICATION TRIGGERED: Status of Task '{}' changed from {} to {}. Sent from {}.",
                task.getTitle(), oldStatus, newStatus, senderEmail);
    }

    /**
     * Send an email with a custom sender address (overload for backward compatibility)
     * Uses default "no-reply@taskmanager.com" as sender
     * 
     * @param recipientEmail Recipient email address
     * @param subject Email subject
     * @param text Email body text
     */
    @Async
    private void sendEmail(String recipientEmail, String subject, String text) {
        sendEmail(recipientEmail, subject, text, "no-reply@taskmanager.com");
    }
    
    /**
     * Send an email with a custom sender address
     * NOTE: The sender address may be overridden by the SMTP server authentication.
     * If emails still come from MAIL_USERNAME, you need to use SMTP relay service
     * or configure your email server to allow custom "from" addresses.
     * 
     * @param recipientEmail Recipient email address
     * @param subject Email subject
     * @param text Email body text
     * @param senderEmail Sender email address (will show in "From" field)
     */
    @Async
    private void sendEmail(String recipientEmail, String subject, String text, String senderEmail) {
        if (recipientEmail == null || recipientEmail.isEmpty()) {
            log.warn("Cannot send email: Recipient email is null or empty.");
            return;
        }
        
        if (senderEmail == null || senderEmail.isEmpty()) {
            senderEmail = "no-reply@taskmanager.com";
            log.warn("Using default sender address: {}", senderEmail);
        }

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(senderEmail);
        message.setTo(recipientEmail);
        message.setSubject(subject);
        message.setText(text);

        try {
            if (mailSender != null) {
                mailSender.send(message);
                log.info("EMAIL SENT successfully from {} to: {} with Subject: '{}'", senderEmail, recipientEmail, subject);
            } else {
                log.error("JavaMailSender is null. Cannot send email. Check Spring configuration.");
            }
        } catch (Exception e) {
            log.error("Failed to send email from {} to {} with subject '{}'. Error: {}", 
                    senderEmail, recipientEmail, subject, e.getMessage(), e);
        }
    }
}