package x10.zenfit.mvp.service.infrastructure.mail.template;

import x10.zenfit.mvp.service.common.domain.entities.order.OrderEntity;
import x10.zenfit.mvp.service.common.domain.entities.order.OrderItem;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.stream.Collectors;

public class OrderInvoiceEmailTemplate {

    private static final Locale VIETNAM = Locale.forLanguageTag("vi-VN");

    private OrderInvoiceEmailTemplate() {
    }

    public static String build(String username, OrderEntity order) {
        String customerName = username == null || username.isBlank()
                ? order.getReceiverName()
                : username;

        String itemRows = order.getItems().stream()
                .map(OrderInvoiceEmailTemplate::formatItemRow)
                .collect(Collectors.joining("\n"));

        return """
                <!doctype html>
                <html lang="vi">
                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <title>X10 Clothing - Xac nhan don hang</title>
                </head>
                <body style="margin:0; padding:0; background:#f4f6f8; font-family:Arial, Helvetica, sans-serif; color:#1f2937;">
                    <div style="max-width:680px; margin:0 auto; padding:24px 12px;">
                        <div style="background:#ffffff; border:1px solid #e5e7eb; border-radius:8px; overflow:hidden;">
                            <div style="background:#111827; padding:22px 28px;">
                                <h1 style="margin:0; color:#ffffff; font-size:22px; line-height:1.35;">X10 Clothing</h1>
                                <p style="margin:6px 0 0; color:#d1d5db; font-size:14px;">Xac nhan don hang</p>
                            </div>

                            <div style="padding:28px;">
                                <p style="margin:0 0 14px; font-size:15px; line-height:1.6;">Xin chao <strong>%s</strong>,</p>
                                <p style="margin:0 0 22px; font-size:15px; line-height:1.6;">X10 Clothing da nhan don hang cua ban. Thong tin chi tiet nhu ben duoi:</p>

                                <table role="presentation" cellpadding="0" cellspacing="0" style="width:100%%; border-collapse:collapse; margin-bottom:22px; border:1px solid #e5e7eb;">
                                    <tbody>
                                        %s
                                    </tbody>
                                </table>

                                <h2 style="margin:0 0 10px; font-size:16px; color:#111827;">San pham</h2>
                                <table role="presentation" cellpadding="0" cellspacing="0" style="width:100%%; border-collapse:collapse; margin-bottom:22px; border:1px solid #e5e7eb;">
                                    <thead>
                                        <tr>
                                            <th align="left" style="padding:12px; background:#f9fafb; border-bottom:1px solid #e5e7eb; font-size:13px;">San pham</th>
                                            <th align="left" style="padding:12px; background:#f9fafb; border-bottom:1px solid #e5e7eb; font-size:13px;">Phan loai</th>
                                            <th align="center" style="padding:12px; background:#f9fafb; border-bottom:1px solid #e5e7eb; font-size:13px;">SL</th>
                                            <th align="right" style="padding:12px; background:#f9fafb; border-bottom:1px solid #e5e7eb; font-size:13px;">Don gia</th>
                                            <th align="right" style="padding:12px; background:#f9fafb; border-bottom:1px solid #e5e7eb; font-size:13px;">Thanh tien</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        %s
                                    </tbody>
                                </table>

                                <table role="presentation" cellpadding="0" cellspacing="0" style="width:100%%; border-collapse:collapse; margin-bottom:24px;">
                                    <tbody>
                                        %s
                                    </tbody>
                                </table>

                                <p style="margin:0; font-size:15px; line-height:1.6;">Cam on ban da mua hang tai <strong>X10 Clothing</strong>.</p>
                            </div>
                        </div>
                    </div>
                </body>
                </html>
                """.formatted(
                escapeHtml(customerName),
                buildOrderInfoRows(order),
                itemRows,
                buildSummaryRows(order)
        );
    }

    private static String buildOrderInfoRows(OrderEntity order) {
        return """
                %s
                %s
                %s
                %s
                %s
                %s
                """.formatted(
                formatInfoRow("Ma don hang", order.getOrderCode()),
                formatInfoRow("Nguoi nhan", order.getReceiverName()),
                formatInfoRow("So dien thoai", order.getReceiverPhone()),
                formatInfoRow("Dia chi", order.getReceiverAddress()),
                formatInfoRow("Phuong thuc thanh toan", order.getPaymentMethod()),
                formatInfoRow("Trang thai thanh toan", order.getPaymentStatus())
        );
    }

    private static String buildSummaryRows(OrderEntity order) {
        return """
                %s
                %s
                %s
                %s
                """.formatted(
                formatSummaryRow("Tam tinh", order.getSubtotal(), false),
                formatSummaryRow("Phi van chuyen", order.getShippingFee(), false),
                formatSummaryRow("Giam gia", order.getDiscountAmount(), false),
                formatSummaryRow("Tong thanh toan", order.getTotalAmount(), true)
        );
    }

    private static String formatInfoRow(String label, Object value) {
        return """
                <tr>
                    <td style="width:38%%; padding:11px 12px; background:#f9fafb; border-bottom:1px solid #e5e7eb; color:#6b7280; font-size:13px;">%s</td>
                    <td style="padding:11px 12px; border-bottom:1px solid #e5e7eb; font-size:14px; color:#111827;">%s</td>
                </tr>
                """.formatted(label, escapeHtml(value));
    }

    private static String formatItemRow(OrderItem item) {
        return """
                <tr>
                    <td style="padding:12px; border-bottom:1px solid #e5e7eb; font-size:14px; color:#111827;">%s</td>
                    <td style="padding:12px; border-bottom:1px solid #e5e7eb; font-size:13px; color:#4b5563;">%s</td>
                    <td align="center" style="padding:12px; border-bottom:1px solid #e5e7eb; font-size:14px; color:#111827;">%d</td>
                    <td align="right" style="padding:12px; border-bottom:1px solid #e5e7eb; font-size:14px; color:#111827;">%s</td>
                    <td align="right" style="padding:12px; border-bottom:1px solid #e5e7eb; font-size:14px; color:#111827; font-weight:600;">%s</td>
                </tr>
                """.formatted(
                escapeHtml(item.getProductName()),
                escapeHtml(formatVariant(item)),
                item.getQuantity() == null ? 0 : item.getQuantity(),
                formatMoney(item.getUnitPrice()),
                formatMoney(item.getTotalPrice())
        );
    }

    private static String formatSummaryRow(String label, BigDecimal value, boolean highlight) {
        String fontSize = highlight ? "16px" : "14px";
        String fontWeight = highlight ? "700" : "400";
        String color = highlight ? "#111827" : "#374151";

        return """
                <tr>
                    <td align="right" style="padding:6px 12px; font-size:%s; color:%s; font-weight:%s;">%s</td>
                    <td align="right" style="width:170px; padding:6px 12px; font-size:%s; color:%s; font-weight:%s;">%s</td>
                </tr>
                """.formatted(
                fontSize,
                color,
                fontWeight,
                label,
                fontSize,
                color,
                fontWeight,
                formatMoney(value)
        );
    }

    private static String formatVariant(OrderItem item) {
        String color = item.getColorName();
        String size = item.getSizeName();

        if ((color == null || color.isBlank()) && (size == null || size.isBlank())) {
            return "-";
        }

        if (color == null || color.isBlank()) {
            return size;
        }

        if (size == null || size.isBlank()) {
            return color;
        }

        return color + " / " + size;
    }

    private static String formatMoney(BigDecimal value) {
        BigDecimal amount = value == null ? BigDecimal.ZERO : value;
        return NumberFormat.getCurrencyInstance(VIETNAM).format(amount);
    }

    private static String escapeHtml(Object value) {
        if (value == null) {
            return "";
        }

        return value.toString()
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }
}
