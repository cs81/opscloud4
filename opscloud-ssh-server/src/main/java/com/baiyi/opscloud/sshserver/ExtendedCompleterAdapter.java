/*
 * Copyright (c) 2020 François Onimus
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.baiyi.opscloud.sshserver;

import org.jline.reader.Candidate;
import org.jline.reader.LineReader;
import org.jline.reader.ParsedLine;
import org.springframework.shell.CompletingParsedLine;
import org.springframework.shell.CompletionContext;
import org.springframework.shell.CompletionProposal;
import org.springframework.shell.Shell;
import org.springframework.shell.jline.JLineShellAutoConfiguration;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Extended completer adapter to be able to set complete attribute of proposal
 * <br>
 * Based on @{@link JLineShellAutoConfiguration.CompleterAdapter}
 */
public class ExtendedCompleterAdapter extends JLineShellAutoConfiguration.CompleterAdapter {

    private final Shell shell;

    public ExtendedCompleterAdapter(Shell shell) {
        this.shell = shell;
    }

    @Override
    public void complete(LineReader reader, ParsedLine line, List<Candidate> candidates) {
        CompletingParsedLine cpl = (line instanceof CompletingParsedLine) ? ((CompletingParsedLine) line) : t -> t;
        CompletionContext context = new CompletionContext(sanitizeInput(line.words()), line.wordIndex(),
                line.wordCursor());

        shell.complete(context).stream().map(p -> new Candidate(
                p.dontQuote() ? p.value() : cpl.emit(p.value()).toString(),
                p.displayText(),
                p.category(),
                p.description(),
                null,
                null,
                isComplete(p)
        )).forEach(candidates::add);
    }

    private static boolean isComplete(CompletionProposal p) {
        if (p instanceof ExtendedCompletionProposal) {
            return ((ExtendedCompletionProposal) p).isComplete();
        }
        return true;
    }

    /**
     * Sanitize the buffer input given the customizations applied to the JLine parser (<em>e.g.</em> support for
     * line continuations, <em>etc.</em>)
     * See @{@link JLineShellAutoConfiguration}
     */
    private static List<String> sanitizeInput(List<String> words) {
        return words.stream()
                // CR at beginning/end of line introduced by backslash
                .map(s -> s.replaceAll("^\\n+|\\n+$", ""))
                // CR in middle of word introduced by return inside a quoted string
                .map(s -> s.replaceAll("\\n+", " "))
                .collect(Collectors.toList());
    }

}
