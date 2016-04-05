package gwt.material.design.addins.client.richeditor;

/*
 * #%L
 * GwtMaterial
 * %%
 * Copyright (C) 2015 - 2016 GwtMaterialDesign
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */


import com.google.gwt.core.client.JsArrayString;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.HasHTML;
import gwt.material.design.addins.client.MaterialResourceInjector;
import gwt.material.design.addins.client.richeditor.base.MaterialRichEditorBase;
import gwt.material.design.addins.client.richeditor.base.constants.ToolbarButton;
import gwt.material.design.client.base.HasPlaceholder;
import gwt.material.design.client.base.MaterialWidget;
import gwt.material.design.client.ui.MaterialToast;

//@formatter:off
/**
 * Provides a great Rich Editor with amazing options built with Material Design Look and Feel
 *
 * <h3>XML Namespace Declaration</h3>
 * <pre>
 * {@code
 * xmlns:m.addins='urn:import:gwt.material.design.addins.client.ui'
 * }
 * </pre>
 *
 * <h3>UiBinder Usage:</h3>
 * <pre>
 *{@code
 * <m.addins:MaterialRichEditor />
 * }
 * </pre>
 *
 * @author kevzlou7979
 * @see <a href="http://gwtmaterialdesign.github.io/gwt-material-demo/snapshot/#richeditor">Material Rich Editor</a>
 */
//@formatter:on
public class MaterialRichEditor extends MaterialRichEditorBase implements HasHTML {

    static {
        if(MaterialResourceInjector.isDebug()) {
            MaterialResourceInjector.injectDebugJs(MaterialRichEditorDebugClientBundle.INSTANCE.richEditorDebugJs());
            MaterialResourceInjector.injectCss(MaterialRichEditorDebugClientBundle.INSTANCE.richEditorDebugCss());
        } else {
            MaterialResourceInjector.injectJs(MaterialRichEditorClientBundle.INSTANCE.richEditorJs());
            MaterialResourceInjector.injectCss(MaterialRichEditorClientBundle.INSTANCE.richEditorCss());
        }
    }

    @Override
    protected void onLoad() {
        super.onLoad();
        initRichEditor(getElement(), isAirMode(), getPlaceholder(), getHeight(), extractOptions(getStyleOptions()), extractOptions(getFontOptions()), extractOptions(getColorOptions()), extractOptions(getUndoOptions()), extractOptions(getCkMediaOptions()), extractOptions(getMiscOptions()), extractOptions(getParaOptions()), extractOptions(getHeightOptions()));
    }

    /**
     * Intialize the rich editor with custom properties
     * @param e
     * @param airMode
     * @param placeholder
     * @param height
     * @param styleOptions
     * @param fontOptions
     * @param colorOptions
     * @param undoOptions
     * @param ckMediaOptions
     * @param miscOptions
     * @param paraOptions
     * @param heightOptions
     */
    private native void initRichEditor(Element e, boolean airMode, String placeholder, String height, JsArrayString styleOptions, JsArrayString fontOptions, JsArrayString colorOptions, JsArrayString undoOptions, JsArrayString ckMediaOptions, JsArrayString miscOptions, JsArrayString paraOptions, JsArrayString heightOptions) /*-{
        var toolbar = [
            ['style', styleOptions],
            ['para', paraOptions],
            ['height', heightOptions],
            ['undo', undoOptions],
            ['fonts', fontOptions],
            ['color', colorOptions],
            ['ckMedia', ckMediaOptions],
            ['misc', miscOptions],
        ];

        $wnd.jQuery(e).materialnote({
            toolbar: toolbar,
            airMode: airMode,
            followingToolbar: false,
            placeholder: placeholder,
            height: height,
            minHeight: 100,
            defaultBackColor: '#777',
            defaultTextColor: '#fff'
        });

    }-*/;

    @Override
    public String getHTML() {
        return getElement().getInnerHTML();
    }

    @Override
    public void setHTML(String html) {
        getElement().setInnerHTML(html);
    }

    @Override
    public String getText() {
        return getElement().getInnerText();
    }

    @Override
    public void setText(String text) {
        getElement().setInnerText(text);
    }

    /**
     * Insert custom text inside the note zone
     * @param text
     */
    public void insertText(String text) {
        insertText(getElement(), text);
    }

    /**
     * Insert custom text inside the note zone with JSNI function
     * @param e
     * @param text
     */
    private native void insertText(Element e, String text) /*-{
        $wnd.jQuery(document).ready(function() {
            $wnd.jQuery(e).materialnote('insertText', text);
        });
    }-*/;

    @Override
    public void clear() {
        clear(getElement());
    }

    /**
     * Clear the note editor with element as param
     * @param e
     */
    private native void clear(Element e) /*-{
        $wnd.jQuery(document).ready(function() {
            $wnd.jQuery(e).materialnote('reset');
        });
    }-*/;
}
