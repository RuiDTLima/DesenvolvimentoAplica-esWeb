package pt.isel.daw.g5.ChecklistAPI.model.internalModel;

import org.springframework.data.domain.Page;
import java.util.stream.Stream;

public class CollectionLink {
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

    public CollectionLink(String rel, String href) {
        this.rel = rel;
        this.href = href;
    }

    public static <T> CollectionLink[] produceLinks(Page<T> page, String href){
        return Stream
                .of(CollectionLink.produceNext(page, href), CollectionLink.producePrevious(page, href))
                .filter(link -> link != null)
                .toArray((size) -> new CollectionLink[size]);
    }

    private static <T> CollectionLink produceNext(Page<T> page, String link){
        if(page.hasNext()) return new CollectionLink("next", link + "?page=" + (page.getNumber() + 2));
        return null;
    }

    private static <T> CollectionLink producePrevious(Page<T> page, String link){
        if(page.getNumber() > 0) return new CollectionLink("previous", link + "?page=" + (page.getNumber()));
        return null;
    }
}