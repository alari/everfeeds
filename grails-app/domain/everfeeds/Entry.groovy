package everfeeds

import everfeeds.manager.IEntry

class Entry implements IEntry {

    String identity
    String title
    String imageUrl
    String content
    String author
    String sourceUrl
    Date placedDate

    Category category
    Account account
    Access access

    private String type

    static hasMany = [tags:Tag]

    static belongsTo = [Access,Account,Category,Tag]

    static transients = ["tagIdentities", "categoryIdentity", "type"]

    static constraints = {
        placedDate index: "placedDateIndex"
        content maxSize: 1024*1024
        author nullable: true
        sourceUrl nullable: true
        imageUrl nullable: true
    }

    List<String> getTagIdentities(){
        tags*.identity
    }

    String getCategoryIdentity(){
        category.identity
    }

    String getType(){
        if(!type) type = access.type
        type
    }
}
