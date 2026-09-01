package com.sara.utils;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class EmailService {

	private static final String MJ_API_KEY =
	        System.getenv("MAILJET_API_KEY");

	private static final String MJ_SECRET_KEY =
	        System.getenv("MAILJET_SECRET_KEY");

	private static final String SENDER_EMAIL =
	        System.getenv("MAILJET_SENDER_EMAIL");
	
    public static void sendNotification(String recipientEmail, String recipientName, String mealName) {
        try {
            String url = "https://api.mailjet.com/v3.1/send";
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            // 1. Prepare Authentication (Basic Auth)
            String auth = MJ_API_KEY + ":" + MJ_SECRET_KEY;
            String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.UTF_8));
            String authHeaderValue = "Basic " + encodedAuth;

            // 2. Set Headers
            con.setRequestMethod("POST");
            con.setRequestProperty("Authorization", authHeaderValue);
            con.setRequestProperty("Content-Type", "application/json");
            con.setDoOutput(true);

            // 3. Construct Mailjet JSON Payload manually
            // Note: Mailjet expects a "Messages" array.
            String jsonInputString = String.format(
                "{" +
                "  \"Messages\": [" +
                "    {" +
                "      \"From\": {" +
                "        \"Email\": \"%s\"," +
                "        \"Name\": \"Coach App\"" +
                "      }," +
                "      \"To\": [" +
                "        {" +
                "          \"Email\": \"%s\"," +
                "          \"Name\": \"%s\"" +
                "        }" +
                "      ]," +
                "      \"Subject\": \"New Meal Added: %s\"," +
                "      \"TextPart\": \"Hello %s! Your coach has just added a new meal to your plan: %s. Log in to check it out!\"" +
                "    }" +
                "  ]" +
                "}",
                SENDER_EMAIL,                // From Email
                recipientEmail, recipientName, // To Email, To Name
                mealName,                    // Subject variable
                recipientName, mealName      // Body variables
            );

            // 4. Send the request
            try (OutputStream os = con.getOutputStream()) {
                byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            // 5. Check response
            int responseCode = con.getResponseCode();
            if (responseCode == 200) {
                System.out.println("✅ Email notification sent successfully to " + recipientEmail);
            } else {
                System.err.println("❌ Failed to send email. Response Code: " + responseCode);
                // Optional: Read the error stream to see what Mailjet says
                // java.io.InputStream err = con.getErrorStream(); 
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("❌ Error calling Mailjet API: " + e.getMessage());
        }
    }
}