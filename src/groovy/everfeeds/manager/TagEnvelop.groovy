package everfeeds.manager

/**
 * Created by alari @ 14.03.11 16:47
 */
class TagEnvelop implements ITag {
    String identity
    String title
    def original

    String toString(){
        "(${identity} -> ${title})"
    }
}
