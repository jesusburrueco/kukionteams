
import dev.failsafe.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.io.*;
import java.net.*;
import java.net.http.HttpClient;
import java.sql.Connection;
import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.Map;

public class Main {

    public static void main(String[] args) throws IOException {

        FirefoxOptions options = new FirefoxOptions();
        FirefoxDriver driver = new FirefoxDriver(options);

        driver.get("https://teams.microsoft.com");

        while(true) {

            WebElement cajadetextoteams = driver.findElement(By.name("ts-message-list-last-item"));
            WebElement cajaderespuestateams = driver.findElement(By.name("ts-editor-section"));
            WebElement submitButtonteams = driver.findElement(By.cssSelector("send-message-button"));

            String pregunta = cajadetextoteams.getText();
            String respuesta = "";
            URL url = new URL("https://devman.kuki.ai/talk");
            Map<String, Object> params = new LinkedHashMap<>();
            params.put("botkey", "YourBotKey");
            params.put("input", pregunta);
            params.put("client_name", "foo");

            StringBuilder postData = new StringBuilder();
            for (Map.Entry<String, Object> param : params.entrySet()) {
                if (postData.length() != 0) postData.append('&');
                postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                postData.append('=');
                postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
            }
            byte[] postDataBytes = postData.toString().getBytes("UTF-8");

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
            conn.setDoOutput(true);
            conn.getOutputStream().write(postDataBytes);

            Reader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

            for (int c; (c = in.read()) >= 0; ) {
                respuesta = respuesta + (char) c;
            }

            cajaderespuestateams.sendKeys(respuesta);

            submitButtonteams.click();

            String[] parts = respuesta.split("\\[");
            String part1 = parts[0];
            String part2 = parts[1];

            String[] parts2 = part2.split("\\]");
            String part3 = parts2[0];
            String part4 = parts2[1];

            System.out.print("Pregunta Usuario: " + pregunta);
            System.out.print("\n");
            System.out.print("Respuesta IA: " + part3);

        }
    }
}


