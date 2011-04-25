/**
 * this describes all the access options
 */
access {

  google {
    oauth {
      key = "everfeeds.com"
      secret = "mucd4gqA1yLtrY6eMzZo3IYe"
      provider = org.scribe.builder.api.GoogleApi
    }
    auth = false
    emailUrl = "https://www.googleapis.com/userinfo/email"
  }

  gmail {
    extend = "google"
    auth = true
    oauth {
      scope = "https://www.googleapis.com/auth/userinfo#email https://mail.google.com/mail/feed/atom/ https://apps-apis.google.com/a/feeds/email_settings/2.0/"
    }
  }

  greader {
    extend = "google"
    auth = true
    oauth {
      scope = "http://www.google.com/reader/api/ http://www.google.com/reader/atom/ https://www.googleapis.com/auth/userinfo#email"
    }
  }

  evernote {
    userAgent = "everfeeds.com"
    host = "www.evernote.com"
    oauth {
      key = "everfeeds"
      secret = "dd0ba24f027198c6"
      provider = org.scribe.builder.api.EvernoteApi
    }
  }

  twitter {
    oauth {
      key = "A5maG2S6WHvloLeFDeIw"
      secret = "2QFVqw7L0GISHTgdB11GHcmhJo970qRmt2Tg10"
      provider = org.scribe.builder.api.TwitterApi
    }
  }

  facebook {
    oauth {
      key = "118265721567840"
      secret = "9d43b1e1ce985e1b3f81d44e51e8cd0f"
      provider = org.scribe.builder.api.FacebookApi
      scope = "publish_stream,offline_access,read_stream,read_mailbox,read_insights"
    }
  }

  linkedin {
    oauth {
      key = "LYuiN2KtQJVJcHOggAZsMT20HzezFqMFvHlAVsaUju5y7gjhc6Y3BJpFLg86QNBX"
      secret = "p93hwB8RY5g7BaEwXc4qZbZhZ7Zqsxl0Rv4TRV9zcW4dBeNuTCfFv5rwjyACUL1U"
      provider = org.scribe.builder.api.LinkedInApi
    }
  }

  vkontakte {
    oauth {
      key = "2300127"
      secret = "eejmLrWGXI7xEYifiU2D"
      provider = org.scribe.builder.api.VkontakteApi
      scope = "friends,wall,offline"
    }
  }
}