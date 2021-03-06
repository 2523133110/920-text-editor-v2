/*
 * Copyright (C) 2016 Jecelyin Peng <jecelyin@gmail.com>
 *
 * This file is part of 920 Text Editor.
 *
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
 */

package com.jecelyin.editor.v2.ui.dialog;

import android.content.Context;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.jecelyin.editor.v2.R;
import com.jecelyin.editor.v2.common.Command;
import com.jecelyin.editor.v2.highlight.jedit.Mode;
import com.jecelyin.editor.v2.highlight.jedit.modes.Catalog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * @author Jecelyin Peng <jecelyin@gmail.com>
 */
public class LangListDialog extends AbstractDialog {
    private String[] scopeList;
    private String[] langList;

    private static class Grammar {
        String name;
        String scope;

        public Grammar(String scope, String name) {
            this.name = name;
            this.scope = scope;
        }
    }

    public LangListDialog(Context context) {
        super(context);

        initGrammarInfo();
    }

    private void initGrammarInfo() {
        Mode[] modes = Catalog.modes;
        ArrayList<Grammar> list = new ArrayList<Grammar>();
        Grammar g;
        Mode mode;
        for (int i=0; i<modes.length; i++) {
            mode = modes[i];
            g = new Grammar(mode.getName(), mode.getName());
            if("Platform".equalsIgnoreCase(g.name))
                continue;
            list.add(g);
        }
        Collections.sort(list, new Comparator<Grammar>() {
            @Override
            public int compare(Grammar lhs, Grammar rhs) {
                return lhs.name.compareToIgnoreCase(rhs.name);
            }
        });

        int size = list.size();
        langList = new String[size];
        scopeList = new String[size];

        for (int i=0; i<size; i++) {
            g = list.get(i);
            langList[i] = g.name;
            scopeList[i] = g.scope;
        }
    }

    @Override
    public void show() {
        MaterialDialog dlg = getDialogBuilder().items(langList)
                .title(R.string.select_lang_to_highlight)
                .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {

                    @Override
                    public boolean onSelection(MaterialDialog materialDialog, View view, int i, CharSequence charSequence) {
                        Command command = new Command(Command.CommandEnum.HIGHLIGHT);
                        command.object = scopeList[i];
                        getMainActivity().doCommand(command);
                        return true;
                    }
                })
                .negativeText(R.string.cancel)
                .show();

        handleDialog(dlg);
    }
}
