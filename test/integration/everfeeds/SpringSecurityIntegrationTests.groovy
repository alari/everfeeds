package everfeeds

import grails.test.*

class SpringSecurityIntegrationTests extends GroovyTestCase {
    def springSecurityService

    protected void setUp() {
        //User.collection.remove([:])
        //Role.collection.remove([:])
        //UserRole.collection.remove([:])
        super.setUp()
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testUser() {
        println "was users: " + Account.list()
        Account u = new Account(username: 'test_user',
                password: springSecurityService.encodePassword('root'),
                enabled: true, accountLocked: false,
                accountExpired: false, passwordExpired: false).save(flush: true)
        println "created ${u.id}: " + Account.list()
        assert u.id

        assert u == Account.get(u.id)

        u.delete()
        println "deleted: " + Account.list()
        System.err << u
        assert Account.findByUsername("test_user")?.id != u.id
        assert !Account.findByUsername('test_user')
    }

    void testRole() {
        Role r = new Role(authority: "TEST_ROLE").save()
        assert r
        r.delete()
        System.err << r
        assert !Role.findByAuthority("TEST_ROLE")
    }

    void testUserRole() {
        Account u = new Account(username: 'test_user2',
                password: springSecurityService.encodePassword('root'),
                enabled: true, accountLocked: false,
                accountExpired: false, passwordExpired: false).save(flush: true)
        Role r = new Role(authority: "TEST_ROLE2").save(flush: true)

        AccountRole.create u, r, true

        assert u.authorities.any {it.authority == r.authority}

        AccountRole.removeAll u
        assertFalse u.authorities.any {it.authority == r.authority}

        AccountRole.create u, r, true

        assert u.authorities.any {it.authority == r.authority}

        AccountRole.remove u, r
        assertFalse u.authorities.any {it.authority == r.authority}


        u.delete()
        r.delete()
    }
}
