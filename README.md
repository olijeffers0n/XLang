<div>
  <img align="left" width="100" height="100" src="https://github.com/Evil-Space-Wizard/XLang/blob/master/img/icon.png">

  XLang provides universal server-side translation to any spigot-powered Minecraft server. It utilizes DeepL's Artificial Intelligence-based translation to output accurate results in any context. It can be used completely for free, and is open-source now and forever. You will require a Free API Key from <a href="https://www.deepl.com/pro/change-plan?/pro?cta=header-prices#developer">DeepL</a> in order to use the translator. For larger servers, it is recommended to use a paid plan instead.
</div>

<h2>Using XLang</h2>
<h4>Why have a translator?</h4>

Your server's native language doesn't matter: there will always be a massive population of players who will never be able to set foot on it if they do not understand what is being told to them. XLang works to amend this by using DeepL's artificial intellgience-assisted translation service to translate both incoming and outgoing chat messages to an abundance of internationally spoken dialects.
<h4>Installing XLang</h4>

XLang does the heavy lifting for you. All you need is a free API Key from <a href="https://www.deepl.com/pro/change-plan?/pro?cta=header-prices#developer">DeepL</a>. This will allow you to translate 500,000 Characters (On Average, roughly 12,500 messages). For large servers, a <a href="https://www.deepl.com/pro/change-plan?/pro?cta=header-prices#developer">DeepL Pro</a> plan is recommended. The Free plan is more than enough for any other size.

1. Install the `XLang.jar` file, whose latest version can be downloaded <a href="releases">here</a>, into your `plugins/` folder. Fully start and stop your server.
2. Edit your `config.yml` file, which can be found in `plugins/XLang`. Set the API Key you received earlier in the `apiKey` slot. The default is `XXX` and will not translate until this is set to a valid API Key.
3. Start your server. Run the command `/xlang setTargetLanguage` and select a language from the Heads GUI menu. If you would like to instead enter a language code, run `/xlang setTargetLanguage <language code>`.
4. Speak in chat, or have other players speak. The message will be translated in the language you selected. New languages and dialects are being added constantly, but due to the nature of the translation service, only a few are supported now. All major languages are supported.

<h4>To-Do (Potential Future Updates)</h4>

- Add support for additional languages and dialects
- Include support for other translation services, such as Google's Translation API

<h4>Known Issues</h4>

Note: Report all issues in the <a href="issues">Issues Tab</a>!
- On some cases, short (one or two word) phrases will not be translated.


The plugin is not tested on versions prior to 1.14, and may face instability.
