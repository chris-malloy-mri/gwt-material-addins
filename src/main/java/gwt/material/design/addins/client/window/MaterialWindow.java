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
package gwt.material.design.addins.client.window;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.logical.shared.*;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.addins.client.MaterialAddins;
import gwt.material.design.addins.client.base.constants.AddinsCssName;
import gwt.material.design.addins.client.dnd.MaterialDnd;
import gwt.material.design.addins.client.dnd.constants.Restriction;
import gwt.material.design.addins.client.dnd.js.JsDragOptions;
import gwt.material.design.client.MaterialDesignBase;
import gwt.material.design.client.base.MaterialWidget;
import gwt.material.design.client.base.mixin.ColorsMixin;
import gwt.material.design.client.base.mixin.ToggleStyleMixin;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.constants.WavesType;
import gwt.material.design.client.ui.MaterialIcon;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.animate.MaterialAnimation;

//@formatter:off

/**
 * Window is another kind of Modal but it has a header toolbar for maximizing and close the window. Also you can attached a tab component on its content.
 *
 * <h3>XML Namespace Declaration</h3>
 * <pre>
 * {@code
 * xmlns:ma='urn:import:gwt.material.design.addins.client'
 * }
 * </pre>
 *
 * <h3>UiBinder Usage:</h3>
 * <pre>
 * {@code
 *  <ma:window.MaterialWindow ui:field="window" />
 * }
 * </pre>
 *
 * <h3>UiBinder Usage:</h3>
 * <pre>
 * {@code
 *  // Opening a window
 *  window.open();
 *
 *  // Closing a window
 *  window.close();
 * }
 * </pre>
 *
 * @author kevzlou7979
 * @see <a href="http://gwtmaterialdesign.github.io/gwt-material-demo/#window">Material Window</a>
 */
//@formatter:on
public class MaterialWindow extends MaterialWidget implements HasCloseHandlers<Boolean>, HasOpenHandlers<Boolean> {

    static {
        if(MaterialAddins.isDebug()) {
            MaterialDesignBase.injectCss(MaterialWindowDebugClientBundle.INSTANCE.windowCssDebug());
        } else {
            MaterialDesignBase.injectCss(MaterialWindowClientBundle.INSTANCE.windowCss());
        }
    }

    private MaterialWidget window = new MaterialWidget(Document.get().createDivElement());
    private MaterialWidget content = new MaterialWidget(Document.get().createDivElement());

    // Toolbar elements
    private MaterialLink link = new MaterialLink();
    private MaterialWidget toolbar = new MaterialWidget(Document.get().createDivElement());
    private MaterialIcon iconMaximize = new MaterialIcon(IconType.CHECK_BOX_OUTLINE_BLANK);
    private MaterialIcon iconClose = new MaterialIcon(IconType.CLOSE);

    private String title = "";
    private Color toolbarColor;

    private final ColorsMixin<MaterialWidget> toolbarColorMixin = new ColorsMixin<>(toolbar);
    private final ToggleStyleMixin<MaterialWidget> maximizeMixin = new ToggleStyleMixin<>(window, AddinsCssName.MAXIMIZE);
    private final ToggleStyleMixin<MaterialWindow> openMixin = new ToggleStyleMixin<>(this, AddinsCssName.OPEN);

    private MaterialAnimation openAnimation;
    private MaterialAnimation closeAnimation;

    public MaterialWindow() {
        super(Document.get().createDivElement(), AddinsCssName.WINDOW_OVERLAY);
        window.setStyleName(AddinsCssName.WINDOW);
        content.setStyleName(AddinsCssName.CONTENT);
        super.add(window);

        initialize();
    }

    /**
     * Builds the toolbar
     */
    protected void initialize() {
        toolbar.setStyleName(AddinsCssName.WINDOW_TOOLBAR);
        link.setStyleName(AddinsCssName.WINDOW_TITLE);
        iconClose.addStyleName(AddinsCssName.WINDOW_ACTION);
        iconMaximize.addStyleName(AddinsCssName.WINDOW_ACTION);
        iconClose.setCircle(true);
        iconClose.setWaves(WavesType.DEFAULT);
        iconMaximize.setCircle(true);
        iconMaximize.setWaves(WavesType.DEFAULT);
        toolbar.add(link);
        toolbar.add(iconClose);
        toolbar.add(iconMaximize);
        toolbar.addDomHandler(event -> {
            toggleMaximize();
            Document.get().getDocumentElement().getStyle().setCursor(Style.Cursor.DEFAULT);
        }, DoubleClickEvent.getType());
        window.add(toolbar);
        window.add(content);

        // Add handlers to action buttons
        iconMaximize.addClickHandler(event -> toggleMaximize());
        iconClose.addClickHandler(event -> {
            if(!isOpen()) {
                open();
            } else {
                close();
            }
        });

        // Add a draggable header
        MaterialDnd dnd = MaterialDnd.draggable(window,
            JsDragOptions.create(new Restriction(Restriction.Restrict.PARENT, true, -0.3, 0, 1.1, 1)));
        dnd.ignoreFrom(".content, .window-action");
    }

    protected void toggleMaximize() {
        setMaximize(!isMaximized());
    }

    @Override
    public void add(Widget child) {
        content.add(child);
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
        link.setText(title);
    }

    public boolean isMaximized() {
        return maximizeMixin.isOn();
    }

    public void setMaximize(boolean maximize) {
        maximizeMixin.setOn(maximize);
        if(maximizeMixin.isOn()) {
            iconMaximize.setIconType(IconType.FILTER_NONE);
        } else {
            iconMaximize.setIconType(IconType.CHECK_BOX_OUTLINE_BLANK);
        }
    }

    /**
     * Open the window.
     */
    public void open() {
        if (!isAttached()) {
            RootPanel.get().add(this);
        }
        OpenEvent.fire(this, true);
        if (openAnimation == null) {
            openMixin.setOn(true);
        } else {
            openAnimation.animate(window, () -> openMixin.setOn(true));
        }
    }

    /**
     * Close the window.
     */
    public void close() {
        CloseEvent.fire(this, false);
        if (closeAnimation == null) {
            openMixin.setOn(false);
        } else {
            closeAnimation.animate(window, () -> openMixin.setOn(false));
        }
    }

    public Color getToolbarColor() {
        return toolbarColor;
    }

    public void setToolbarColor(Color toolbarColor) {
        this.toolbarColor = toolbarColor;
        toolbarColorMixin.setBackgroundColor(toolbarColor);
    }

    @Deprecated
    public void setToolbarColor(String toolbarColor) {
        setToolbarColor(Color.fromStyleName(toolbarColor));
    }

    public void setOpenAnimation(final MaterialAnimation openAnimation) {
        this.openAnimation = openAnimation;
    }

    public void setCloseAnimation(final MaterialAnimation closeAnimation) {
        this.closeAnimation = closeAnimation;
    }

    @Override
    public HandlerRegistration addCloseHandler(final CloseHandler<Boolean> handler) {
        return addHandler(handler, CloseEvent.getType());
    }

    @Override
    public HandlerRegistration addOpenHandler(final OpenHandler<Boolean> handler) {
        return addHandler(handler, OpenEvent.getType());
    }

    @Override
    public void setWidth(String width) {
        window.setWidth(width);
    }

    @Override
    public void setHeight(String height) {
        window.setHeight(height);
    }

    @Override
    public void setBackgroundColor(Color bgColor) {
        window.setBackgroundColor(bgColor);
    }

    @Deprecated
    @Override
    public void setBackgroundColor(String bgColor) {
        window.setBackgroundColor(bgColor);
    }

    public boolean isOpen() {
        return openMixin.isOn();
    }
}
