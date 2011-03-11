package everfeeds

class Account {

	String username
	String password
	boolean enabled
	boolean accountExpired
	boolean accountLocked
	boolean passwordExpired

    String evernoteUser
    String evernoteToken
    String evernoteShard

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
}
