package x10.zenfit.mvp.service.service.orderService;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import x10.zenfit.mvp.service.Repository.IOrderRepository;
import x10.zenfit.mvp.service.common.domain.entities.order.OrderEntity;
import x10.zenfit.mvp.service.common.domain.entities.order.OrderItem;
import x10.zenfit.mvp.service.common.domain.enums.OrderStatus;
import x10.zenfit.mvp.service.common.domain.enums.PaymentMethod;
import x10.zenfit.mvp.service.common.domain.enums.PaymentStatus;
import x10.zenfit.mvp.service.share.exception.BusinessException;
import x10.zenfit.mvp.service.share.exception.order.OrderError;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class DeliveryNotePdfService {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final Locale VIETNAM = new Locale("vi", "VN");

    private final IOrderRepository orderRepository;

    public byte[] generate(String orderId) {
        OrderEntity order = orderRepository.findById(orderId)
                .or(() -> orderRepository.findByOrderCode(orderId))
                .orElseThrow(() -> new BusinessException(OrderError.ORDER_NOT_FOUND));

        ensureCanExport(order);

        try {
            return renderPdf(order);
        } catch (DocumentException | IOException ex) {
            throw new BusinessException(
                    OrderError.INVALID_ORDER_DATA,
                    "Khong the tao phieu giao hang PDF"
            );
        }
    }

    public String buildFileName(String orderId) {
        OrderEntity order = orderRepository.findById(orderId)
                .or(() -> orderRepository.findByOrderCode(orderId))
                .orElseThrow(() -> new BusinessException(OrderError.ORDER_NOT_FOUND));

        String orderCode = hasText(order.getOrderCode()) ? order.getOrderCode() : order.getId();
        return "delivery-note-" + orderCode + ".pdf";
    }

    private byte[] renderPdf(OrderEntity order) throws DocumentException, IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A5, 28, 28, 28, 28);
        PdfWriter.getInstance(document, outputStream);

        BaseFont baseFont = resolveBaseFont();
        Font titleFont = new Font(baseFont, 18, Font.BOLD);
        Font subtitleFont = new Font(baseFont, 14, Font.BOLD);
        Font labelFont = new Font(baseFont, 10, Font.BOLD);
        Font bodyFont = new Font(baseFont, 10, Font.NORMAL);
        Font smallFont = new Font(baseFont, 9, Font.NORMAL);
        Font moneyFont = new Font(baseFont, 10, Font.BOLD);

        document.open();

        addCentered(document, "POLOMAN", titleFont);
        addCentered(document, "PHIẾU GIAO HÀNG", subtitleFont);
        addSeparator(document, bodyFont);

        addInfoRow(document, "Mã đơn", value(order.getOrderCode()), labelFont, bodyFont);
        addInfoRow(document, "Ngày", formatDate(order.getCreatedAt()), labelFont, bodyFont);
        addInfoRow(document, "Khách hàng", resolveCustomerName(order), labelFont, bodyFont);
        addInfoRow(document, "SĐT", value(order.getReceiverPhone()), labelFont, bodyFont);
        addInfoRow(document, "Địa chỉ", value(order.getReceiverAddress()), labelFont, bodyFont);
        addSeparator(document, bodyFont);

        Paragraph productTitle = new Paragraph("Sản phẩm", labelFont);
        productTitle.setSpacingAfter(8);
        document.add(productTitle);

        List<OrderItem> items = order.getItems() == null ? List.of() : order.getItems();
        for (OrderItem item : items) {
            addProductBlock(document, item, bodyFont, smallFont);
        }

        addSeparator(document, bodyFont);
        addMoneyRow(document, "Tổng tiền", order.getSubtotal(), labelFont, moneyFont);
        addMoneyRow(document, "Ship", order.getShippingFee(), labelFont, moneyFont);
        addMoneyRow(document, "Voucher", order.getDiscountAmount(), labelFont, moneyFont);
        addMoneyRow(document, "Thanh toán", order.getTotalAmount(), labelFont, moneyFont);
        addMoneyRow(document, "Tiền cần thu", calculateCollectAmount(order), labelFont, moneyFont);
        addSeparator(document, bodyFont);

        Paragraph note = new Paragraph("Vui lòng kiểm tra hàng trước khi giao/nhận.", smallFont);
        note.setAlignment(Element.ALIGN_CENTER);
        note.setSpacingBefore(8);
        document.add(note);

        document.close();
        return outputStream.toByteArray();
    }

    private void ensureCanExport(OrderEntity order) {
        OrderStatus status = normalizeStatus(order.getStatus());
        if (status != OrderStatus.CONFIRMED
                && status != OrderStatus.PACKING
                && status != OrderStatus.SHIPPING
                && status != OrderStatus.COMPLETED) {
            throw new BusinessException(
                    OrderError.INVALID_ORDER_STATUS,
                    "Chi co the xuat phieu giao hang khi don hang da xac nhan"
            );
        }
    }

    private OrderStatus normalizeStatus(OrderStatus status) {
        if (status == OrderStatus.DELIVERED) {
            return OrderStatus.COMPLETED;
        }
        return status;
    }

    private void addCentered(Document document, String text, Font font) throws DocumentException {
        Paragraph paragraph = new Paragraph(text, font);
        paragraph.setAlignment(Element.ALIGN_CENTER);
        paragraph.setSpacingAfter(6);
        document.add(paragraph);
    }

    private void addSeparator(Document document, Font font) throws DocumentException {
        Paragraph separator = new Paragraph("────────────────────────────", font);
        separator.setAlignment(Element.ALIGN_CENTER);
        separator.setSpacingBefore(6);
        separator.setSpacingAfter(8);
        document.add(separator);
    }

    private void addInfoRow(Document document, String label, String value, Font labelFont, Font bodyFont) throws DocumentException {
        PdfPTable table = new PdfPTable(new float[]{1.1f, 2.4f});
        table.setWidthPercentage(100);
        table.setSpacingAfter(5);
        table.addCell(cell(label, labelFont, Element.ALIGN_LEFT));
        table.addCell(cell(value, bodyFont, Element.ALIGN_LEFT));
        document.add(table);
    }

    private void addProductBlock(Document document, OrderItem item, Font bodyFont, Font smallFont) throws DocumentException {
        Paragraph name = new Paragraph(resolveProductName(item), bodyFont);
        name.setSpacingAfter(3);
        document.add(name);

        document.add(new Paragraph("SL: " + defaultQuantity(item), smallFont));
        document.add(new Paragraph("Đơn giá: " + formatMoney(item == null ? null : item.getUnitPrice()), smallFont));
        Paragraph total = new Paragraph("Thành tiền: " + formatMoney(item == null ? null : item.getTotalPrice()), smallFont);
        total.setSpacingAfter(8);
        document.add(total);
    }

    private void addMoneyRow(Document document, String label, BigDecimal value, Font labelFont, Font moneyFont) throws DocumentException {
        PdfPTable table = new PdfPTable(new float[]{1.4f, 1.3f});
        table.setWidthPercentage(100);
        table.setSpacingAfter(4);
        table.addCell(cell(label, labelFont, Element.ALIGN_LEFT));
        table.addCell(cell(formatMoney(value), moneyFont, Element.ALIGN_RIGHT));
        document.add(table);
    }

    private PdfPCell cell(String text, Font font, int alignment) {
        PdfPCell cell = new PdfPCell(new Phrase(value(text), font));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(alignment);
        cell.setPadding(1);
        return cell;
    }

    private BigDecimal calculateCollectAmount(OrderEntity order) {
        if (order.getPaymentMethod() == PaymentMethod.COD && !isPaid(order.getPaymentStatus())) {
            return defaultMoney(order.getTotalAmount());
        }
        return BigDecimal.ZERO;
    }

    private boolean isPaid(PaymentStatus status) {
        return status == PaymentStatus.PAID || status == PaymentStatus.SUCCESS;
    }

    private String resolveCustomerName(OrderEntity order) {
        if (hasText(order.getReceiverName())) {
            return order.getReceiverName();
        }
        if (hasText(order.getGuestUsername())) {
            return order.getGuestUsername();
        }
        return "Khách hàng";
    }

    private String resolveProductName(OrderItem item) {
        if (item == null) {
            return "";
        }

        StringBuilder builder = new StringBuilder(value(item.getProductName()));
        if (hasText(item.getColorName())) {
            builder.append(" ").append(item.getColorName());
        }
        if (hasText(item.getSizeName())) {
            builder.append(" ").append(item.getSizeName());
        }
        return builder.toString().trim();
    }

    private String formatDate(LocalDateTime value) {
        return value == null ? "" : value.format(DATE_FORMATTER);
    }

    private String formatMoney(BigDecimal value) {
        NumberFormat formatter = NumberFormat.getNumberInstance(VIETNAM);
        formatter.setMaximumFractionDigits(0);
        formatter.setMinimumFractionDigits(0);
        return formatter.format(defaultMoney(value)) + "đ";
    }

    private BigDecimal defaultMoney(BigDecimal value) {
        return value == null ? BigDecimal.ZERO : value;
    }

    private int defaultQuantity(OrderItem item) {
        return item == null || item.getQuantity() == null ? 0 : item.getQuantity();
    }

    private String value(String value) {
        return value == null ? "" : value;
    }

    private boolean hasText(String value) {
        return value != null && !value.isBlank();
    }

    private BaseFont resolveBaseFont() throws DocumentException, IOException {
        String[] candidates = {
                "C:/Windows/Fonts/arial.ttf",
                "C:/Windows/Fonts/tahoma.ttf",
                "/usr/share/fonts/truetype/dejavu/DejaVuSans.ttf",
                "/usr/share/fonts/truetype/liberation/LiberationSans-Regular.ttf"
        };

        for (String candidate : candidates) {
            if (Files.exists(Path.of(candidate))) {
                return BaseFont.createFont(candidate, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            }
        }

        return BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI, BaseFont.NOT_EMBEDDED);
    }
}
