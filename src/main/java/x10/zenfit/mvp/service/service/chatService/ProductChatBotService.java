package x10.zenfit.mvp.service.service.chatService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import x10.zenfit.mvp.service.Repository.ICategoryRepository;
import x10.zenfit.mvp.service.Repository.IProductRepository;
import x10.zenfit.mvp.service.common.domain.entities.product.CategoryEntity;
import x10.zenfit.mvp.service.common.domain.entities.product.ColorVariantEntity;
import x10.zenfit.mvp.service.common.domain.entities.product.ProductEntity;
import x10.zenfit.mvp.service.common.domain.entities.product.SizeVariantEntity;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductChatBotService {

    private static final int MAX_RESULTS = 3;
    private static final Pattern SPLIT_PATTERN = Pattern.compile("[^\\p{L}\\p{N}]+");
    private static final Set<String> STOP_WORDS = Set.of(
            "toi", "minh", "em", "anh", "chi", "ban", "shop", "co", "khong", "ko", "k",
            "la", "ve", "cho", "can", "muon", "tim", "mua", "tu", "van", "san", "pham",
            "hang", "mau", "size", "gia", "bao", "nhieu", "nao", "gi", "nhe", "a", "oi",
            "ao", "quan"
    );

    private final IProductRepository productRepository;
    private final ICategoryRepository categoryRepository;
    private final OpenAiChatAdvisorService openAiChatAdvisorService;

    public Optional<String> answer(String question) {
        if (!StringUtils.hasText(question)) {
            return Optional.empty();
        }

        List<String> keywords = extractKeywords(question);
        if (keywords.isEmpty()) {
            return Optional.empty();
        }

        Map<String, String> categoryNames = categoryRepository.findAll().stream()
                .collect(Collectors.toMap(CategoryEntity::getId, CategoryEntity::getName, (first, second) -> first));

        List<ScoredProduct> matches = productRepository.findAll().stream()
                .filter(product -> !Boolean.FALSE.equals(product.getActive()))
                .map(product -> score(product, categoryNames.get(product.getCategoryId()), keywords))
                .filter(scored -> scored.score() > 0)
                .sorted(Comparator.comparingInt(ScoredProduct::score).reversed())
                .limit(MAX_RESULTS)
                .toList();

        if (matches.isEmpty()) {
            return Optional.empty();
        }

        String productContext = buildProductContext(matches, categoryNames);
        Optional<String> llmAnswer = openAiChatAdvisorService.advise(question, productContext);
        if (llmAnswer.isPresent()) {
            return llmAnswer;
        }

        return Optional.of(buildAnswer(matches, categoryNames));
    }

    private List<String> extractKeywords(String question) {
        String normalized = normalize(question);
        Set<String> keywords = new HashSet<>();
        for (String token : SPLIT_PATTERN.split(normalized)) {
            if (token.length() >= 2 && !STOP_WORDS.contains(token)) {
                keywords.add(token);
            }
        }
        return new ArrayList<>(keywords);
    }

    private ScoredProduct score(ProductEntity product, String categoryName, List<String> keywords) {
        String searchable = normalize(String.join(" ",
                safe(product.getName()),
                safe(product.getDescription()),
                safe(categoryName),
                safe(product.getSlug()),
                colors(product)
        ));

        int score = 0;
        for (String keyword : keywords) {
            if (searchable.contains(keyword)) {
                score += productNameContains(product, keyword) ? 3 : 1;
            }
        }

        return new ScoredProduct(product, score);
    }

    private boolean productNameContains(ProductEntity product, String keyword) {
        return normalize(product.getName()).contains(keyword);
    }

    private String colors(ProductEntity product) {
        if (product.getColors() == null) {
            return "";
        }
        return product.getColors().stream()
                .map(ColorVariantEntity::getColorName)
                .filter(StringUtils::hasText)
                .collect(Collectors.joining(" "));
    }

    private String buildAnswer(List<ScoredProduct> matches, Map<String, String> categoryNames) {
        StringBuilder answer = new StringBuilder("Minh tim thay mot vai san pham phu hop trong shop:\n");

        for (int index = 0; index < matches.size(); index++) {
            ProductEntity product = matches.get(index).product();
            answer.append(index + 1)
                    .append(". ")
                    .append(product.getName())
                    .append(" - ")
                    .append(formatPrice(product))
                    .append(". ");

            String categoryName = categoryNames.get(product.getCategoryId());
            if (StringUtils.hasText(categoryName)) {
                answer.append("Danh muc: ").append(categoryName).append(". ");
            }

            String variants = summarizeVariants(product);
            if (StringUtils.hasText(variants)) {
                answer.append(variants).append(". ");
            }

            if (StringUtils.hasText(product.getSlug())) {
                answer.append("Slug: ").append(product.getSlug()).append(".");
            }

            answer.append("\n");
        }

        answer.append("Neu ban cho minh biet them mau, size hoac tam gia, minh se goi y sat hon.");
        return answer.toString();
    }

    private String buildProductContext(List<ScoredProduct> matches, Map<String, String> categoryNames) {
        StringBuilder context = new StringBuilder();
        for (ScoredProduct match : matches) {
            ProductEntity product = match.product();
            context.append("- Ten: ").append(safe(product.getName())).append("\n")
                    .append("  Slug: ").append(safe(product.getSlug())).append("\n")
                    .append("  Danh muc: ").append(safe(categoryNames.get(product.getCategoryId()))).append("\n")
                    .append("  Gia goc: ").append(product.getPrice()).append("\n")
                    .append("  Gia sale: ").append(product.getSalePrice()).append("\n")
                    .append("  Mo ta: ").append(safe(product.getDescription())).append("\n")
                    .append("  Bien the: ").append(summarizeVariants(product)).append("\n");
        }
        return context.toString();
    }

    private String formatPrice(ProductEntity product) {
        BigDecimal effectivePrice = product.getSalePrice() != null ? product.getSalePrice() : product.getPrice();
        if (effectivePrice == null) {
            return "chua co gia";
        }

        NumberFormat formatter = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("vi-VN"));
        String formatted = formatter.format(effectivePrice);
        if (product.getSalePrice() != null && product.getPrice() != null
                && product.getSalePrice().compareTo(product.getPrice()) < 0) {
            return formatted + " (dang giam tu " + formatter.format(product.getPrice()) + ")";
        }
        return formatted;
    }

    private String summarizeVariants(ProductEntity product) {
        if (product.getColors() == null || product.getColors().isEmpty()) {
            return "";
        }

        List<String> colors = new ArrayList<>();
        Set<String> sizes = new HashSet<>();
        int totalQuantity = 0;

        for (ColorVariantEntity color : product.getColors()) {
            if (StringUtils.hasText(color.getColorName())) {
                colors.add(color.getColorName());
            }
            if (color.getSizes() == null) {
                continue;
            }
            for (SizeVariantEntity size : color.getSizes()) {
                if (StringUtils.hasText(size.getSize())) {
                    sizes.add(size.getSize());
                }
                if (size.getQuantity() != null) {
                    totalQuantity += size.getQuantity();
                }
            }
        }

        List<String> parts = new ArrayList<>();
        if (!colors.isEmpty()) {
            parts.add("Mau: " + String.join(", ", colors.stream().limit(4).toList()));
        }
        if (!sizes.isEmpty()) {
            parts.add("Size: " + sizes.stream().sorted().limit(6).collect(Collectors.joining(", ")));
        }
        if (totalQuantity > 0) {
            parts.add("con khoang " + totalQuantity + " san pham");
        }
        return String.join("; ", parts);
    }

    private String normalize(String value) {
        if (!StringUtils.hasText(value)) {
            return "";
        }
        return value.toLowerCase(Locale.ROOT).trim();
    }

    private String safe(String value) {
        return value == null ? "" : value;
    }

    private record ScoredProduct(ProductEntity product, int score) {
    }
}
