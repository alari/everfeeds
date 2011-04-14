package everfeeds

import org.springframework.security.core.GrantedAuthority
import org.bson.types.ObjectId

class Role implements GrantedAuthority {

    static mapWith = "mongo"

    ObjectId id

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
