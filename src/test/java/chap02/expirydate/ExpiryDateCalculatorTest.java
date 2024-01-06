package chap02.expirydate;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

public class ExpiryDateCalculatorTest {

    private void assertExpiryDate(PayData payData, LocalDate expectedExpiryDate) {
        ExpiryDateCalculator cal = new ExpiryDateCalculator();
        LocalDate realExpiryDate = cal.calculateExpiryDate(payData);
        assertEquals(expectedExpiryDate, realExpiryDate);
    }

    @Test
    void 만원_납부하면_한달_뒤가_만료일이_됨() {
        assertExpiryDate(
                PayData.builder()
                        .billingDate(LocalDate.of(2019, 03, 01))
                        .payAmount(10_000)
                        .build(),
                LocalDate.of(2019, 04, 01)
        );
        assertExpiryDate(
                PayData.builder()
                        .billingDate(LocalDate.of(2023, 05, 05))
                        .payAmount(10_000)
                        .build(),
                LocalDate.of(2023, 06, 05)
        );
    }

    @Test
    void 납부일과_한달_뒤_일자가_같지_않음() {
        assertExpiryDate(
                PayData.builder()
                        .billingDate(LocalDate.of(2019, 01, 31))
                        .payAmount(10_000)
                        .build(),
                LocalDate.of(2019, 02, 28)
        );
        assertExpiryDate(
                PayData.builder()
                        .billingDate(LocalDate.of(2019, 05, 31))
                        .payAmount(10_000)
                        .build(),
                LocalDate.of(2019, 06, 30)
        );
        assertExpiryDate(
                PayData.builder()
                        .billingDate(LocalDate.of(2020, 01, 31))
                        .payAmount(10_000)
                        .build(),
                LocalDate.of(2020, 02, 29)
        );
    }

    @Test
    void 첫_납부일과_만료일_일자가_다를때_만원_납부시_첫_납부일_기준으로() {
        PayData payData = PayData.builder()
                .firstBillingDate(LocalDate.of(2019, 01, 31))
                .billingDate(LocalDate.of(2019, 02, 28))
                .payAmount(10_000)
                .build();

        assertExpiryDate(payData, LocalDate.of(2019, 03, 31));

        PayData payData2 = PayData.builder()
                .firstBillingDate(LocalDate.of(2019, 01, 30))
                .billingDate(LocalDate.of(2019, 02, 28))
                .payAmount(10_000)
                .build();

        assertExpiryDate(payData2, LocalDate.of(2019, 03, 30));

        PayData payData3 = PayData.builder()
                .firstBillingDate(LocalDate.of(2019, 05, 31))
                .billingDate(LocalDate.of(2019, 06, 30))
                .payAmount(10_000)
                .build();

        assertExpiryDate(payData3, LocalDate.of(2019, 07, 31));
    }

    @Test
    void 이만원_이상_납부하면_비례해서_만료일_계산() {
        assertExpiryDate(
                PayData.builder()
                        .billingDate(LocalDate.of(2019, 03, 01))
                        .payAmount(20_000)
                        .build(),
                LocalDate.of(2019, 05, 01)
        );
        assertExpiryDate(
                PayData.builder()
                        .billingDate(LocalDate.of(2019, 03, 01))
                        .payAmount(30_000)
                        .build(),
                LocalDate.of(2019, 06, 01)
        );
        assertExpiryDate(
                PayData.builder()
                        .billingDate(LocalDate.of(2019, 03, 01))
                        .payAmount(70_000)
                        .build(),
                LocalDate.of(2019, 10, 01)
        );
    }

    @Test
    void 첫_납부일과_만료일_일자가_다를때_이만원_이상_납부() {
        assertExpiryDate(
                PayData.builder()
                        .firstBillingDate(LocalDate.of(2019, 01, 31))
                        .billingDate(LocalDate.of(2019, 02, 28))
                        .payAmount(20_000)
                        .build(),
                LocalDate.of(2019, 04, 30)
        );
        assertExpiryDate(
                PayData.builder()
                        .firstBillingDate(LocalDate.of(2019, 01, 31))
                        .billingDate(LocalDate.of(2019, 02, 28))
                        .payAmount(40_000)
                        .build(),
                LocalDate.of(2019, 06, 30)
        );
        assertExpiryDate(
                PayData.builder()
                        .firstBillingDate(LocalDate.of(2019, 03, 31))
                        .billingDate(LocalDate.of(2019, 04, 28))
                        .payAmount(30_000)
                        .build(),
                LocalDate.of(2019, 07, 31)
        );
    }

    @Test
    void 십만원_납부하면_1년_제공() {
        assertExpiryDate(
                PayData.builder()
                        .billingDate(LocalDate.of(2019, 01, 28))
                        .payAmount(100_000)
                        .build(),
                LocalDate.of(2020, 01, 28)
        );
    }
}
