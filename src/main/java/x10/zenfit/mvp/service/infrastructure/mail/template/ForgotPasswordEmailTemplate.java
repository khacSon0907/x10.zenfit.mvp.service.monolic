package x10.zenfit.mvp.service.infrastructure.mail.template;

public class ForgotPasswordEmailTemplate {

    private ForgotPasswordEmailTemplate() {
    }

    public static String build(String username, String otp) {
        return """
                <!doctype html>
                <html>
                <body style="margin:0; padding:0; background:#f3f4f6; font-family:Arial, Helvetica, sans-serif; color:#111827;">
                    <table role="presentation" cellpadding="0" cellspacing="0" style="width:100%%; background:#f3f4f6; padding:28px 12px;">
                        <tr>
                            <td align="center">
                                <table role="presentation" cellpadding="0" cellspacing="0" style="width:100%%; max-width:560px; background:#ffffff; border:1px solid #e5e7eb; border-radius:8px; overflow:hidden;">
                                    <tr>
                                        <td style="background:#111827; padding:22px 26px;">
                                            <h1 style="margin:0; color:#ffffff; font-size:22px; line-height:1.35;">X10 Clothing</h1>
                                            <p style="margin:6px 0 0; color:#d1d5db; font-size:14px;">Password reset</p>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td style="padding:28px 26px;">
                                            <p style="margin:0 0 14px; font-size:15px; line-height:1.6;">Hello <strong>%s</strong>,</p>
                                            <p style="margin:0 0 22px; font-size:15px; line-height:1.6;">We received a request to reset the password for your X10 Clothing account. Use the code below to continue.</p>
                                            <div style="margin:0 0 22px; padding:18px 20px; background:#f9fafb; border:1px solid #e5e7eb; border-radius:8px; text-align:center;">
                                                <div style="font-size:13px; color:#6b7280; margin-bottom:8px;">Password reset code</div>
                                                <div style="font-size:34px; line-height:1.2; font-weight:700; letter-spacing:8px; color:#111827;">%s</div>
                                            </div>
                                            <p style="margin:0 0 14px; font-size:14px; line-height:1.6; color:#374151;">This code will expire in <strong>5 minutes</strong>.</p>
                                            <p style="margin:0; font-size:13px; line-height:1.6; color:#6b7280;">If you did not request a password reset, you can safely ignore this email.</p>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td style="padding:18px 26px; background:#f9fafb; border-top:1px solid #e5e7eb; color:#6b7280; font-size:12px; line-height:1.5;">
                                            X10 Clothing Team
                                        </td>
                                    </tr>
                                </table>
                            </td>
                        </tr>
                    </table>
                </body>
                </html>
                """.formatted(escapeHtml(username), escapeHtml(otp));
    }

    private static String escapeHtml(String value) {
        if (value == null) {
            return "";
        }

        return value
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }
}
