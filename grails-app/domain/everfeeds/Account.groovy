package everfeeds

class Account {

  String username
  String password
  boolean enabled
  boolean accountExpired
  boolean accountLocked
  boolean passwordExpired

  static hasMany = [accesses: Access, entries: Entry]

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

  Account addAuthority(Role role, boolean flush = false) {
    AccountRole.create this, role, flush
    this
  }

  Account addAuthority(String authority, boolean flush = false) {
    addAuthority Role.getByAuthority(authority), flush
  }

  String toString() {
    username
  }
}
