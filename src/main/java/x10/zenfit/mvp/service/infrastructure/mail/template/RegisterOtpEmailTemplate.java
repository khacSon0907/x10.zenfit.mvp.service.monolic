package x10.zenfit.mvp.service.infrastructure.mail.template;

public class RegisterOtpEmailTemplate {

    private RegisterOtpEmailTemplate() {
    }

    public static String build(
            String username,
            String otp
    ) {
        return """
                <!doctype html>
                <html lang="vi">
                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <title>Xác thực tài khoản Zenfit</title>
                </head>
                <body style="margin:0; padding:0; background:#f4f6f8; font-family:'Segoe UI', Arial, sans-serif; color:#1e293b;">
                    <table role="presentation" cellpadding="0" cellspacing="0" style="width:100%%; background:#f4f6f8; padding:32px 12px;">
                        <tr>
                            <td align="center">
                                <table role="presentation" cellpadding="0" cellspacing="0" style="width:100%%; max-width:540px; background:#ffffff; border:1px solid #e2e8f0; border-radius:14px; overflow:hidden; box-shadow:0 4px 16px rgba(0,0,0,0.05);">
                                    <!-- Header -->
                                    <tr>
                                        <td style="background:linear-gradient(135deg, #059669 0%%, #047857 100%%); padding:26px 30px; text-align:left;">
                                            <h1 style="margin:0; color:#ffffff; font-size:24px; font-weight:800; letter-spacing:-0.5px; line-height:1.2;">Zenfit</h1>
                                            <p style="margin:6px 0 0; color:#a7f3d0; font-size:13px; font-weight:500;">Nền tảng rèn luyện &amp; dinh dưỡng toàn diện</p>
                                        </td>
                                    </tr>

                                    <!-- Body -->
                                    <tr>
                                        <td style="padding:32px 30px;">
                                            <p style="margin:0 0 12px; font-size:16px; font-weight:600; color:#0f172a;">
                                                Xin chào <span style="color:#059669;">%s</span> 👋,
                                            </p>
                                            <p style="margin:0 0 20px; font-size:14px; line-height:1.65; color:#475569;">
                                                Chào mừng bạn đến với <strong>Zenfit</strong>! Hãy sử dụng mã xác thực bên dưới để hoàn tất việc kích hoạt tài khoản và bắt đầu hành trình nâng tầm thể lực của bạn.
                                            </p>

                                            <!-- OTP Code Box -->
                                            <div style="margin:24px 0; padding:22px; background:#f0fdf4; border:1.5px dashed #86efac; border-radius:10px; text-align:center;">
                                                <div style="font-size:12px; font-weight:600; text-transform:uppercase; letter-spacing:1.2px; color:#15803d; margin-bottom:10px;">
                                                    MÃ XÁC THỰC CỦA BẠN (OTP)
                                                </div>
                                                <div style="font-size:36px; line-height:1.1; font-weight:800; letter-spacing:10px; color:#065f46; font-family:'Courier New', Courier, monospace;">
                                                    %s
                                                </div>
                                            </div>

                                            <!-- Security notice -->
                                            <div style="margin:0 0 20px; padding:12px 16px; background:#f8fafc; border-left:3px solid #059669; border-radius:4px;">
                                                <p style="margin:0; font-size:13px; line-height:1.5; color:#475569;">
                                                    ⏱ Mã này có hiệu lực trong vòng <strong>5 phút</strong>.<br>
                                                    🔒 Tuyệt đối không chia sẻ mã này cho bất kỳ ai để bảo vệ tài khoản của bạn.
                                                </p>
                                            </div>

                                            <p style="margin:0; font-size:13px; line-height:1.6; color:#94a3b8;">
                                                Nếu bạn không thực hiện đăng ký tài khoản trên Zenfit, vui lòng bỏ qua email này.
                                            </p>
                                        </td>
                                    </tr>

                                    <!-- Footer -->
                                    <tr>
                                        <td style="padding:18px 30px; background:#f8fafc; border-top:1px solid #f1f5f9; text-align:center; color:#64748b; font-size:12px; line-height:1.6;">
                                            <p style="margin:0; font-weight:600; color:#334155;">Đội ngũ Zenfit</p>
                                            <p style="margin:3px 0 0; color:#94a3b8;">Khỏe mạnh hơn mỗi ngày cùng Zenfit</p>
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
