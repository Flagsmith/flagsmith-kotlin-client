package com.example.flagsmith

import com.fasterxml.jackson.databind.node.TextNode
import org.springframework.stereotype.Controller
import org.springframework.util.StringUtils
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.servlet.ModelAndView
import com.flagsmith.exceptions.FlagsmithApiError
import com.flagsmith.exceptions.FlagsmithClientError
import com.flagsmith.kotlin.FlagsmithClient
import com.flagsmith.models.BaseFlag
import com.flagsmith.models.DefaultFlag
import com.flagsmith.models.Flags
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

@Controller
class HelloController {
    @GetMapping("")
    @ResponseBody
    @Throws(FlagsmithApiError::class, FlagsmithClientError::class)
    fun index(
        @RequestParam(name = "identifier", defaultValue = "") identifier: String,
        @RequestParam(name = "trait-key", defaultValue = "") traitKey: String,
        @RequestParam(name = "trait-value", defaultValue = "") traitValue: String
    ): ModelAndView {
        val featureName: String = "secret_button"
        val flags: Flags
        flags = if (StringUtils.hasLength(identifier)) {
            val traits: HashMap<String, Any> = HashMap<String, Any>()
            if (StringUtils.hasLength(traitKey) && StringUtils.hasLength(traitValue)) {
                traits.put(traitKey, traitValue)
            }
            flagsmith.getIdentityFlags(identifier!!, traits)
        } else {
            flagsmith.getEnvironmentFlags()
        }
        val showButton: Boolean? = flags.isFeatureEnabled(featureName)
        val value: Any = flags.getFeatureValue(featureName)
        val buttonValue = if (value is String) value else (value as TextNode).textValue()
        val fontColor: FontColour? = this.parse(buttonValue)

        val view = ModelAndView()
        view.setViewName("index")
        view.addObject("show_button", showButton ?: false)
        view.addObject("font_colour", fontColor!!.colour)
        view.addObject("identifier", if (StringUtils.hasLength(identifier)) identifier else false )

        return view
    }

    private fun parse(data: String): FontColour? {
        return Json.decodeFromString<FontColour>(data)
    }

    companion object {
        private val flagsmith: FlagsmithClient = FlagsmithClient
            .Builder()
            .defaultFlagHandler( fun (featureName: String): BaseFlag {
                val flag = DefaultFlag()
                flag.setEnabled(false)
                if (featureName.equals("secret_button")) {
                    flag.setValue("{\"colour\": \"#ababab\"}")
                } else {
                    flag.setValue(null)
                }
                return flag
            })
            .apiKey("SERVER_KEY")
            .build()

    }
}