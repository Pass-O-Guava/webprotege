package edu.stanford.bmir.protege.web.client.irigen.obo;

import com.google.common.base.Optional;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.client.ui.editor.ValueEditor;
import edu.stanford.bmir.protege.web.client.ui.editor.ValueEditorFactory;
import edu.stanford.bmir.protege.web.client.ui.editor.ValueListEditorImpl;
import edu.stanford.bmir.protege.web.shared.irigen.SuffixSettingsEditor;
import edu.stanford.bmir.protege.web.shared.irigen.SuffixSettingsId;
import edu.stanford.bmir.protege.web.shared.irigen.obo.OBOSuffixSettings;
import edu.stanford.bmir.protege.web.shared.irigen.obo.UserIdRange;

import java.util.Collections;
import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 30/07/2013
 */
public class OBOIdSuffixSettingsEditor extends Composite implements SuffixSettingsEditor<OBOSuffixSettings> {

    interface OBOIdSchemeSpecificSettingsEditorUiBinder extends UiBinder<HTMLPanel, OBOIdSuffixSettingsEditor> {

    }

    private static OBOIdSchemeSpecificSettingsEditorUiBinder ourUiBinder = GWT.create(OBOIdSchemeSpecificSettingsEditorUiBinder.class);

    @UiField
    protected HasText labelLangEditor;

    @UiField
    protected HasText totalDigitsEditor;

    @UiField(provided = true)
    protected ValueListEditorImpl<UserIdRange> userRangeTable;


    public OBOIdSuffixSettingsEditor() {
        userRangeTable = new ValueListEditorImpl<UserIdRange>(new ValueEditorFactory<UserIdRange>() {
            @Override
            public ValueEditor<UserIdRange> createEditor() {
                return new UserIdRangeEditorImpl();
            }
        });
        HTMLPanel rootElement = ourUiBinder.createAndBindUi(this);
        initWidget(rootElement);
    }

    @Override
    public SuffixSettingsId getSupportedSuffixSettingsId() {
        return OBOSuffixSettings.Id;
    }

    @Override
    public void setSettings(OBOSuffixSettings settings) {
        totalDigitsEditor.setText(Integer.toString(settings.getTotalDigits()));
        userRangeTable.setValue(settings.getUserIdRanges());
        labelLangEditor.setText(settings.getLabelLanguage().or(""));
    }

    @Override
    public OBOSuffixSettings getSettings() {
        List<UserIdRange> userIdRanges = getUserIdRanges();
        Optional<String> language = getLanguage();
        return new OBOSuffixSettings(getTotalDigits(), userIdRanges, language);
    }

    private Optional<String> getLanguage() {
        final String text = labelLangEditor.getText().trim();
        if(text.isEmpty()) {
            return Optional.absent();
        }
        else {
            return Optional.of(text);
        }
    }

    private List<UserIdRange> getUserIdRanges() {
//        List<UserIdRange> userIdRanges = new ArrayList<UserIdRange>();
//        for(int rowIndex = 0; rowIndex < userRangeTable.getRowCount(); rowIndex++) {
//            Widget widget = userRangeTable.getWidget(rowIndex, 0);
//            Optional<UserIdRange> value = ((UserIdRangeEditor) widget).getValue();
//            if (value.isPresent()) {
//                userIdRanges.add(value.get());
//            }
//        }
//        return userIdRanges;
        if(userRangeTable.getValue().isPresent()) {
            return userRangeTable.getValue().get();
        }
        else {
            return Collections.emptyList();
        }
    }

    private int getTotalDigits() {
        try {
            return Integer.parseInt(totalDigitsEditor.getText().trim());
        }
        catch (NumberFormatException e) {
            return OBOSuffixSettings.DEFAULT_TOTAL_DIGITS;
        }
    }

}