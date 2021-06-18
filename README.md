<div>
  <img align="left" width="100" height="100" src="https://github.com/olijeffers0n/XLang/blob/master/img/icon.png">

  XLang provides universal server-side translation to any spigot-powered Minecraft server. It utilizes DeepL's Artificial Intelligence-based translation to output accurate results in any context. It can be used completely for free, and is open-source now and forever. You will require a Free API Key from <a href="https://www.deepl.com/pro/change-plan?/pro?cta=header-prices#developer">DeepL</a> in order to use the translator. For larger servers, it is recommended to use a paid plan instead.
</div>

## Using XLang
#### Why have a translator?

Your server's native language doesn't matter: there will always be a massive population of players who will never be able to set foot on it if they do not understand what is being told to them. XLang works to amend this by using DeepL's artificial intellgience-assisted translation service to translate both incoming and outgoing chat messages to an abundance of internationally spoken dialects. <a href="https://github.com/olijeffers0n/XLang/wiki/Supported-Languages">View Supported Languages</a>
#### Installing XLang

XLang does the heavy lifting for you. All you need is a free API Key from <a href="https://www.deepl.com/pro/change-plan?/pro?cta=header-prices#developer">DeepL</a>. This will allow you to translate 500,000 Characters (On Average, roughly 12,500 messages) Per month. For large servers, a <a href="https://www.deepl.com/pro/change-plan?/pro?cta=header-prices#developer">DeepL Pro</a> plan is recommended. The Free plan is more than enough for any other size.

1. Install the `XLang.jar` file, whose latest version can be downloaded <a href="https://github.com/olijeffers0n/XLang/releases">here</a>, into your `plugins/` folder. Fully start and stop your server.
2. Edit your `config.yml` file, which can be found in `plugins/XLang`. Set the API Key you received earlier in the `apiKey` slot. The default is `XXX` and will not translate until this is set to a valid API Key.
3. Start your server. Run the command `/xlang setTargetLanguage` and select a language from the Heads GUI menu. If you would like to instead enter a language code, run `/xlang setTargetLanguage <language code>`.
4. Run the command `/xlang setTranslationMode` and select either:
   - serverWide : One language will be enforced across your entire server
   - perPlayerLocale : Translates the messages for the player based on their client locale. Use `/setLang` to choose an alternate language.
5. Speak in chat, or have other players speak. The message will be translated in the language you selected. New languages and dialects are being added constantly, but due to the nature of the translation service, only a few are supported now. All major languages are supported.

### Permissions

 - XLang.* - Permission for all XLang actions
 - XLang.getUsedCharacters - Gets the current Deepl Usage
 - XLang.setlanguage - Sets the server-wide language
 - Xlang.setmode - Allows you to change the locale from per-player to server wide

### Commands

 - /setlang (/setlanguage, /lang) - Allows you to set your preference for your personal Locale
 - /XLang - Master Command for:
	 - getUsedCharacters - Returns the usage data from Deepl
	 - setTargetLanguage - Allows you to set the language. Either provide a code or nothing to show a GUI.
	 - setTranslationMode - Allows you to choose between:
			 - perPlayerLocale - translates messages per player so everyone gets their preferred language. This will use more translation "characters" from Deepl
			 - serverWide - Has one server wide language all messages will be translated to
	 - reload - Will reload the config
	
#### To-Do (Potential Future Updates)

- Add support for additional languages and dialects
- Include support for other translation services, such as Google's Translation API

#### Known Issues

Note: Report all issues in the <a href="https://github.com/olijeffers0n/XLang/issues">Issues Tab</a>!
- On some cases, short (one or two word) phrases will not be translated.

- This is not an issue however the plugin will download dependencies to the `libraries` folder in your server. This requires one of the latest versions of spigot so you should run [BuildTools](https://www.spigotmc.org/wiki/buildtools/) to get the latest

The plugin is not tested on versions prior to 1.16.5, and it is unlikely to work. 
