package everfeeds.manager

/**
 * Created by alari @ 14.03.11 16:34
 */
class CategoryEnvelop implements ICategory{
    String identity
    String title

    def original

    String toString(){
        "(${identity} -> ${title})"
    }
}
