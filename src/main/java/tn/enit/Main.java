package tn.enit;

import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.worker.JobWorker;
import io.camunda.zeebe.client.impl.oauth.OAuthCredentialsProvider;
import io.camunda.zeebe.client.impl.oauth.OAuthCredentialsProviderBuilder;
import tn.enit.handler.ClientNotifierHandler;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {

    private static final String ZEEBE_ADDRESS = "fce6c506-e698-4137-9fc7-2d9ccadccaa9.syd-1.zeebe.camunda.io:443";
    private static final String ZEEBE_CLIENT_ID = "y-WGm4tRgzykLOtkQ9HGnk0yoVmKvld5";
    private static final String ZEEBE_CLIENT_SECRET = "174gu0q87Ss2ALADT_Da1FyEknYecDy7r7zoo~7Mf3iyEMMc.uRH0yKLE2F7~UYY";
    private static final String ZEEBE_AUTHORIZATION_SERVER_URL = "https://login.cloud.camunda.io/oauth/token";
    private static final String ZEEBE_TOKEN_AUDIENCE = "zeebe.camunda.io";
    //private static final String DEMANDE_DE_RESERVATION_JOB_TYPE = "envoyerDemandeDeResevation";
    //private static final String MESSAGE_NAME = "msg_reservationConfirmee";

    public static void main(String[] args) {

        final Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("num_carte_de_credit", "C8_12345");



        final OAuthCredentialsProvider credentialsProvider =
                new OAuthCredentialsProviderBuilder()
                        .authorizationServerUrl(ZEEBE_AUTHORIZATION_SERVER_URL)
                        .audience(ZEEBE_TOKEN_AUDIENCE)
                        .clientId(ZEEBE_CLIENT_ID)
                        .clientSecret(ZEEBE_CLIENT_SECRET)
                        .build();

        try (final ZeebeClient client =
                     ZeebeClient.newClientBuilder()
                             .gatewayAddress(ZEEBE_ADDRESS)
                             .credentialsProvider(credentialsProvider)
                             .build()) {
            // System.out.println("Connected to: " + client.newTopologyRequest().send().join());
            client.newCreateInstanceCommand()
                    .bpmnProcessId("process_payment")
                    .latestVersion()
                    .variables(variables)
                    .send()
                    .join();

            final JobWorker notifierAssureWorker =
                    client.newWorker()
                            .jobType("valider")
                            .handler(new ClientNotifierHandler())
                            .timeout(Duration.ofSeconds(10).toMillis())
                            .open();
                            Thread.sleep(10000);
            //Wait for the Workers
            Scanner sc = new Scanner(System.in);
            sc.nextInt();
            sc.close();
            notifierAssureWorker.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //System.out.println("Hello world!");
    }
}