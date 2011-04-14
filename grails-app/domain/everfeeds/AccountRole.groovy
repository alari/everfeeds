package everfeeds

import org.apache.commons.lang.builder.HashCodeBuilder
import org.bson.types.ObjectId

class AccountRole implements Serializable {

    static mapWith = "mongo"

    ObjectId id

    Account account
    Role role

    boolean equals(other) {
        if (!(other instanceof AccountRole)) {
            return false
        }

        other.account?.id == account?.id &&
                other.role?.id == role?.id
    }

    int hashCode() {
        def builder = new HashCodeBuilder()
        if (account) builder.append(account.id)
        if (role) builder.append(role.id)
        builder.toHashCode()
    }

    static AccountRole get(ObjectId userId, ObjectId roleId) {
        AccountRole.collection.findOne([user: userId, role: roleId])
    }

    static AccountRole create(Account user, Role role, boolean flush = false) {
        new AccountRole(account: user, role: role).save(flush: flush, insert: true)
    }

    static boolean remove(Account account, Role role, boolean flush = false) {
        AccountRole instance = AccountRole.findByAccountAndRole(account, role)
        instance ? instance.delete(flush: flush) : false
    }

    static void removeAll(Account account) {
        AccountRole.collection.remove([account: account.id])
    }

    static void removeAll(Role role) {
        AccountRole.collection.remove([role: role.id])
    }

    static mapping = {
        version false
    }
}
