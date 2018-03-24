package pt.isel.daw.g5.ChecklistAPI.model.internalModel;

import org.springframework.data.domain.Page;
import java.util.stream.Stream;

public class Link {
    private String rel;
    private String href;

    public String getRel() {
        return rel;
    }

    public void setRel(String rel) {
        this.rel = rel;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public Link(String rel, String href) {
        this.rel = rel;
        this.href = href;
    }

    public static <T> Link[] produceLinks(Page<T> page, String href){
        return Stream
                .of(Link.produceNext(page, href), Link.producePrevious(page, href))
                .filter(link -> link != null)
                .toArray((size) -> new Link[size]);
    }

    private static <T> Link produceNext(Page<T> page, String link){
        if(page.hasNext()) return new Link("next", link + "?page=" + (page.getNumber() + 2));
        return null;
    }

    private static <T> Link producePrevious(Page<T> page, String link){
        if(page.getNumber() > 0) return new Link("previous", link + "?page=" + (page.getNumber()));
        return null;
    }
}