package everfeeds

import org.springframework.security.core.GrantedAuthority

class Role implements GrantedAuthority {

	String authority

	static mapping = {
		cache true
	}

	static constraints = {
		authority blank: false, unique: true
	}

    static Role getByAuthority(String authority){
        Role.findByAuthority(authority) ?: new Role(authority: authority).save()
    }
}
