// package com.lezhin.clone.backend.service;

// import lombok.SneakyThrows;

// import org.apache.commons.codec.binary.Base64;
// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.core.ParameterizedTypeReference;
// import org.springframework.http.HttpEntity;
// import org.springframework.http.HttpMethod;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.MediaType;
// import org.springframework.http.ResponseEntity;
// import org.springframework.stereotype.Service;
// import org.springframework.util.LinkedMultiValueMap;
// import org.springframework.util.MultiValueMap;
// import org.springframework.web.client.RestTemplate;
// import org.springframework.web.multipart.MultipartFile;
// import org.springframework.web.server.ResponseStatusException;

// import com.google.api.client.auth.oauth2.BearerToken;
// import com.google.api.client.auth.oauth2.Credential;
// import com.google.api.client.auth.oauth2.TokenResponse;
// import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
// import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
// import com.google.api.client.googleapis.json.GoogleJsonError;
// import com.google.api.client.googleapis.json.GoogleJsonResponseException;
// import com.google.api.client.http.HttpHeaders;
// import com.google.api.client.http.HttpRequestInitializer;
// import com.google.api.client.http.HttpTransport;
// import com.google.api.client.http.javanet.NetHttpTransport;
// import com.google.api.client.json.JsonFactory;
// import com.google.api.client.json.gson.GsonFactory;
// import com.google.api.services.gmail.Gmail;
// import com.google.api.services.gmail.GmailScopes;
// import com.google.api.services.gmail.model.Draft;
// import com.google.api.services.gmail.model.Message;
// import com.google.auth.http.HttpCredentialsAdapter;
// import com.google.auth.oauth2.GoogleCredentials;
// import com.lezhin.clone.backend.dto.GmailCredential;

// import jakarta.activation.DataHandler;
// import jakarta.activation.DataSource;
// import jakarta.activation.FileDataSource;
// import jakarta.mail.Authenticator;
// import jakarta.mail.MessagingException;
// import jakarta.mail.Multipart;
// import jakarta.mail.PasswordAuthentication;
// import jakarta.mail.Session;
// import jakarta.mail.internet.InternetAddress;
// import jakarta.mail.internet.MimeBodyPart;
// import jakarta.mail.internet.MimeMessage;
// import jakarta.mail.internet.MimeMultipart;
// import jakarta.mail.util.ByteArrayDataSource;

// import java.io.ByteArrayOutputStream;
// import java.io.File;
// import java.io.IOException;
// import java.util.Map;
// import java.util.Properties;

// @Service
// public class GmailService {

//     private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
//     private HttpTransport httpTransport;
//     private GmailCredential gmailCredential;
//     @Value("${spring.google.client-id}")
//     private String clientId;
//     @Value("${spring.google.client-secret}")
//     private String secretKey;
//     @Value("${spring.google.refresh-token}")
//     private String refreshToken;
//     @Value("${spring.google.from-email}")
//     private String fromEmail;

//     @SneakyThrows
//     public GmailService() {

//         this.httpTransport = GoogleNetHttpTransport.newTrustedTransport();

//         this.gmailCredential = new GmailCredential(
//                 clientId,
//                 secretKey,
//                 refreshToken,
//                 null,
//                 null,
//                 fromEmail);

//     }

//     public static Message sendEmail(String fromEmailAddress,
//             String toEmailAddress)
//             throws MessagingException, IOException {
//         /*
//          * Load pre-authorized user credentials from the environment.
//          * TODO(developer) - See https://developers.google.com/identity for
//          * guides on implementing OAuth2 for your application.
//          */
//         GoogleCredentials credentials = GoogleCredentials.getApplicationDefault()
//                 .createScoped(GmailScopes.GMAIL_SEND);
//                 new GoogleCredentials()
//         HttpRequestInitializer requestInitializer = new HttpCredentialsAdapter(credentials);

//         // Create the gmail API client
//         Gmail service = new Gmail.Builder(new NetHttpTransport(),
//                 GsonFactory.getDefaultInstance(),
//                 requestInitializer)
//                 .setApplicationName("Gmail samples")
//                 .build();

//         // Create the email content
//         String messageSubject = "Test message";
//         String bodyText = "lorem ipsum.";

//         // Encode as MIME message
//         Properties props = new Properties();
//         Session session = Session.getDefaultInstance(props, null);
//         MimeMessage email = new MimeMessage(session);
//         email.setFrom(new InternetAddress(fromEmailAddress));
//         email.addRecipient(jakarta.mail.Message.RecipientType.TO,
//                 new InternetAddress(toEmailAddress));
//         email.setSubject(messageSubject);
//         email.setText(bodyText);

//         // Encode and wrap the MIME message into a gmail message
//         ByteArrayOutputStream buffer = new ByteArrayOutputStream();
//         email.writeTo(buffer);
//         byte[] rawMessageBytes = buffer.toByteArray();
//         String encodedEmail = Base64.encodeBase64URLSafeString(rawMessageBytes);
//         Message message = new Message();
//         message.setRaw(encodedEmail);

//         try {
//             // Create send message
//             message = service.users().messages().send("me", message).execute();
//             System.out.println("Message id: " + message.getId());
//             System.out.println(message.toPrettyString());
//             return message;
//         } catch (GoogleJsonResponseException e) {
//             // TODO(developer) - handle error appropriately
//             GoogleJsonError error = e.getDetails();
//             if (error.getCode() == 403) {
//                 System.err.println("Unable to send message: " + e.getDetails());
//             } else {
//                 throw e;
//             }
//         }
//         return null;
//     }

//     public MimeMessage createEmail(String toEmailAddress,
//             String fromEmailAddress,
//             String subject,
//             String bodyText)
//             throws MessagingException {
//         Properties props = new Properties();
//         Session session = Session.getDefaultInstance(props, null);

//         MimeMessage email = new MimeMessage(session);

//         email.setFrom(new InternetAddress(fromEmailAddress));
//         email.addRecipient(jakarta.mail.Message.RecipientType.TO,
//                 new InternetAddress(toEmailAddress));
//         email.setSubject(subject);
//         email.setText(bodyText);
//         return email;
//     }

//     public Message createMessageWithEmail(MimeMessage emailContent) throws MessagingException, IOException {
//         ByteArrayOutputStream buffer = new ByteArrayOutputStream();
//         emailContent.writeTo(buffer);
//         byte[] bytes = buffer.toByteArray();
//         String encodedEmail = Base64.encodeBase64URLSafeString(bytes);
//         Message message = new Message();
//         message.setRaw(encodedEmail);
//         return message;
//     }

//     public Draft createDraftMessageWithAttachment(String fromEmailAddress,
//             String toEmailAddress,
//             File file)
//             throws MessagingException, IOException {
//         /*
//          * Load pre-authorized user credentials from the environment.
//          * TODO(developer) - See https://developers.google.com/identity for
//          * guides on implementing OAuth2 for your application.
//          */
//         GoogleCredentials credentials = GoogleCredentials.getApplicationDefault()
//                 .createScoped(GmailScopes.GMAIL_COMPOSE);
//         HttpRequestInitializer requestInitializer = new HttpCredentialsAdapter(credentials);

//         // Create the gmail API client
//         Gmail service = new Gmail.Builder(new NetHttpTransport(),
//                 GsonFactory.getDefaultInstance(),
//                 requestInitializer)
//                 .setApplicationName("Gmail samples")
//                 .build();

//         // Create the email content
//         String messageSubject = "Test message";
//         String bodyText = "lorem ipsum.";

//         // Encode as MIME message
//         Properties props = new Properties();
//         Session session = Session.getDefaultInstance(props, null);
//         MimeMessage email = new MimeMessage(session);
//         email.setFrom(new InternetAddress(fromEmailAddress));
//         email.addRecipient(jakarta.mail.Message.RecipientType.TO,
//                 new InternetAddress(toEmailAddress));
//         email.setSubject(messageSubject);

//         MimeBodyPart mimeBodyPart = new MimeBodyPart();
//         mimeBodyPart.setContent(bodyText, "text/plain");
//         Multipart multipart = new MimeMultipart();
//         multipart.addBodyPart(mimeBodyPart);
//         mimeBodyPart = new MimeBodyPart();
//         DataSource source = new FileDataSource(file);
//         mimeBodyPart.setDataHandler(new DataHandler(source));
//         mimeBodyPart.setFileName(file.getName());
//         multipart.addBodyPart(mimeBodyPart);
//         email.setContent(multipart);

//         // Encode and wrap the MIME message into a gmail message
//         ByteArrayOutputStream buffer = new ByteArrayOutputStream();
//         email.writeTo(buffer);
//         byte[] rawMessageBytes = buffer.toByteArray();
//         String encodedEmail = Base64.encodeBase64URLSafeString(rawMessageBytes);
//         Message message = new Message();
//         message.setRaw(encodedEmail);

//         try {
//             // Create the draft message
//             Draft draft = new Draft();
//             draft.setMessage(message);
//             draft = service.users().drafts().create("me", draft).execute();
//             System.out.println("Draft id: " + draft.getId());
//             System.out.println(draft.toPrettyString());
//             return draft;
//         } catch (GoogleJsonResponseException e) {
//             // TODO(developer) - handle error appropriately
//             GoogleJsonError error = e.getDetails();
//             if (error.getCode() == 403) {
//                 System.err.println("Unable to create draft: " + e.getDetails());
//             } else {
//                 throw e;
//             }
//         }
//         return null;
//     }
// }