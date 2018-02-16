/*
 * #%L
 * GwtMaterial
 * %%
 * Copyright (C) 2015 - 2018 GwtMaterialDesign
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
package gwt.material.design.incubator.client.question;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.base.MaterialWidget;
import gwt.material.design.client.base.helper.ScrollHelper;
import gwt.material.design.incubator.client.base.constants.IncubatorCssName;
import gwt.material.design.incubator.client.question.base.QuestionItem;
import gwt.material.design.incubator.client.question.base.QuestionProgress;

import java.util.ArrayList;
import java.util.List;

public class QuestionFieldGroup extends MaterialWidget {

    private List<QuestionItem> questions = new ArrayList<>();
    private List<QuestionItem> answeredQuestions = new ArrayList<>();
    private QuestionProgress questionProgress;

    public QuestionFieldGroup() {
        super(Document.get().createFormElement(), IncubatorCssName.QUESTION_FIELD_GROUP);
    }

    @Override
    protected void onLoad() {
        super.onLoad();

        questionProgress = new QuestionProgress(questions);
        add(questionProgress);

        load();
    }

    protected void load() {
        for (Widget widget : getChildren()) {
            lookForChildren(widget);
        }

        for (QuestionItem question : questions) {
            question.addValueChangeHandler(valueChangeEvent -> {
                int index = questions.indexOf(question);
                if (index + 1 < questions.size()) {
                    Widget target = questions.get(index + 1);
                    new ScrollHelper().scrollTo(target);
                }

                updateProgress(question);
            });
        }

    }

    @Override
    protected void onUnload() {
        super.onUnload();

        unload();
    }

    protected void unload() {
        //reset();
    }

    public void reload() {
        unload();
        load();
    }

    protected void lookForChildren(Widget parent) {
        if (parent instanceof HasWidgets) {
            for (Widget widget : ((HasWidgets) parent)) {
                if (widget instanceof QuestionItem) {
                    ((QuestionItem) widget).setAllowBlank(false);
                    questions.add((QuestionItem) widget);
                } else {
                    lookForChildren(widget);
                }
            }
        }
    }

    protected void updateProgress(QuestionItem question) {
        if (!answeredQuestions.contains(question)) {
            answeredQuestions.add(question);
            questionProgress.updateProgress(answeredQuestions);
        }
    }

    public List<QuestionItem> getQuestions() {
        return questions;
    }

    public void setQuestions(List<QuestionItem> questions) {
        this.questions = questions;
    }

    public void showQuestionProgress(boolean visible) {
        questionProgress.setVisible(visible);
    }

    public void reset() {
        for (QuestionItem question : questions) {
            question.reset();
        }

        questionProgress.reset();
        answeredQuestions.clear();
    }

    @Override
    public boolean validate() {
        boolean valid = super.validate();

        for (QuestionItem question : questions) {
            if (!question.isValid()) {
                new ScrollHelper().scrollTo(question);
                break;
            }
        }

        return valid;
    }
}
