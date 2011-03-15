package everfeeds

class Account {

	String username
	String password
	boolean enabled
	boolean accountExpired
	boolean accountLocked
	boolean passwordExpired

    static hasMany = [accesses:Access]

	static constraints = {
		username blank: false, unique: true
		password blank: false
	}

	static mapping = {
		password column: '`password`'
	}

	Set<Role> getAuthorities() {
		AccountRole.findAllByAccount(this).collect { it.role } as Set
	}

    String toString(){
        username
    }

    Set<Feed> getFeeds(){
        accesses.collect{it.feeds}.flatten()
    }
}
