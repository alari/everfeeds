package everfeeds

import org.bson.types.ObjectId

class Account {

    static mapWith = "mongo"

    ObjectId id

	String username
	String password
	boolean enabled
	boolean accountExpired
	boolean accountLocked
	boolean passwordExpired

    List<Access> accesses

    static hasMany = [accesses:Access, entries:Entry]

	static constraints = {
		username blank: false, unique: true
		password blank: false
	}

	static mapping = {
		password column: '`password`'
	}

    static transients = ["authorities"]

	Set<Role> getAuthorities() {
		AccountRole.findAllByAccount(this).collect { it.role } as Set
	}

    String toString(){
        username
    }
}
