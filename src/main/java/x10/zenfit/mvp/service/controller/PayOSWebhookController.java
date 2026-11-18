package x10.zenfit.mvp.service.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.payos.model.webhooks.Webhook;
import x10.zenfit.mvp.service.service.paymentService.socket.PaymentWebhookService;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PayOSWebhookController {

    private final PaymentWebhookService paymentWebhookService;

    @PostMapping("/payos-webhook")
    public ResponseEntity<Map<String, Object>> handlePayOSWebhook(@RequestBody Webhook webhook) {
        log.info("Received PayOS webhook: {}", webhook);
        try {
            paymentWebhookService.handleWebhook(webhook);
            return ResponseEntity.ok(Map.of("success", true, "message", "Webhook processed successfully"));
        } catch (Exception ex) {
            log.error("Error processing PayOS webhook: {}", ex.getMessage(), ex);
            return ResponseEntity.ok(Map.of("success", false, "error", ex.getMessage()));
        }
    }
}
