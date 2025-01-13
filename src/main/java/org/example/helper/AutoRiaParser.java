package org.example.helper;

import org.example.model.AutoRiaCars;
import org.example.model.CarDetails;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Component
public class AutoRiaParser {

    private static final String BASE_URL = "https://auto.ria.com/uk/search/";

    private String buildUrl(long chatId, Map<Long, CarDetails> carDetailsMap) {
        CarDetails details = carDetailsMap.get(chatId);
        ParameterID parameterID = new ParameterID();
        StringBuilder urlBuilder = new StringBuilder(BASE_URL);
        return urlBuilder.append("?categories.main.id=1")
                         .append("&price.currency=1")
                         .append("&price.USD.gte=").append(details.getStartPrice())
                         .append("&price.USD.lte=").append(details.getEndPrice())
                         .append("&indexName=auto,order_auto,newauto_search&region.id[0]=10")
                         .append("&brand.id[0]=").append(parameterID.getBrandID(details.getBrand()))
                         .append("&model.id[0]=").append(parameterID.getModelID(details.getBrand(), details.getModel()))
                         .append("&year[0].gte=").append(details.getStartYear())
                         .append("&year[0].lte=").append(details.getEndYear())
                         .append("&size=5")
                         .toString();
    }

    private Document fetchPage(String url) throws Exception {
        return Jsoup.connect(url)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/131.0.0.0 Safari/537.36")
                .get();
    }

    private List<AutoRiaCars> parseCars(Document page) {
        List<AutoRiaCars> cars = new ArrayList<>();
        Elements carItems = page.select(".ticket-item");

        for (Element car : carItems) {
            //Name
            String mark = Objects.requireNonNull(car.selectFirst("div.hide")).attr("data-mark-name");
            String model = Objects.requireNonNull(car.selectFirst("div.hide")).attr("data-model-name");

            //Price
            Element priceElement = car.selectFirst("div.price-ticket");
            String price = priceElement != null ? priceElement.attr("data-main-price") : "the price is not specified";

            //Year
            Element yearElement = car.selectFirst("div.hide");
            String year = yearElement != null ? yearElement.attr("data-year") : "year is not specified";

            //Mileage
            Element mileageElement = car.selectFirst("li.item-char.js-race");
            String mileage = mileageElement != null ? mileageElement.ownText().trim() : "mileage not specified";

            //Region
            Element regionElement = car.selectFirst("li.view-location.js-location");
            String region = regionElement != null ? regionElement.ownText().trim() : "the region is not specified";

            //FuelType
            Element fuelTypeElement = car.selectFirst("li.item-char:has(i.icon-fuel)");
            String fuelType = fuelTypeElement != null ? fuelTypeElement.ownText().trim() : "the type of fuel is not specified";

            //GearBox
            Element gearBoxTypeElement = car.selectFirst("li.item-char:has(i.icon-transmission)");
            String gearBoxType = gearBoxTypeElement != null ? gearBoxTypeElement.ownText().trim() : "The type of gearbox is not specified";

            //Description
            Element descriptionElement = car.selectFirst("p.descriptions-ticket");
            String description = descriptionElement != null ? descriptionElement.text() : "there is no description";

            //Date
            Element addDataElement = car.selectFirst(".footer_ticket span[data-add-date]");
            String addDate = addDataElement != null ? addDataElement.attr("data-add-date") : "noData";

            //link
            String link = car.select(".ticket-title a").attr("href");

            cars.add(new AutoRiaCars(mark, model, price, year, mileage, region, fuelType, gearBoxType, description, addDate, link));
        }

        return cars;
    }

    public List<AutoRiaCars> parse(long chatId, Map<Long, CarDetails> carDetailsMap) throws Exception {
        String url = buildUrl(chatId, carDetailsMap);
        Document page = fetchPage(url);
        List<AutoRiaCars> cars = parseCars(page);

        return cars;
    }
}
