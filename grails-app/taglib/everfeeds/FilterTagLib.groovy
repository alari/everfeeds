package everfeeds

class FilterTagLib {
    /**
     * @attr with
     * @attr without
     * @attr obj
     */
    def filterCls = {attrs->
        out << ((attrs.with as List).contains(attrs.obj) ? "with" : ((attrs.without as List).contains(attrs.obj) ? "without" : ""))
    }
}
