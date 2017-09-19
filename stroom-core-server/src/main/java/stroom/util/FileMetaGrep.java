/*
 * Copyright 2016 Crown Copyright
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

package stroom.util;

import stroom.feed.MetaMap;
import stroom.streamstore.server.fs.BlockGZIPInputFile;
import stroom.streamstore.server.fs.UncompressedInputStream;
import stroom.streamstore.server.fs.serializable.RANestedInputStream;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.stream.Stream;

public class FileMetaGrep extends AbstractCommandLineTool {
    Map<String, String> matchMap;
    private String[] repoPathParts = null;
    private String feedId;

    public FileMetaGrep(String[] args) throws Exception {
        matchMap = ArgsUtil.parse(args);
        matchMap.remove("repoPath");
        matchMap.remove("feedId");

        doMain(args);
    }

    public static void main(String[] args) throws Exception {
        new FileMetaGrep(args);
    }

    public void setRepoPath(String repoPath) {
        this.repoPathParts = repoPath.split("/");
    }

    public void setFeedId(String feedId) {
        this.feedId = feedId;
    }

    @Override
    public void run() {
        StringBuilder path = new StringBuilder();

        for (String part : repoPathParts) {
            if (part.contains("*")) {
                break;
            }
            path.append("/");
            path.append(part);
        }

        scanDir(Paths.get(path.toString()));
    }

    private void scanDir(Path root) {
        try (final Stream<Path> stream = Files.walk(root)) {
            stream.forEach(p -> {
                if (matches(p.toAbsolutePath().normalize().toString())) {
                    if (Files.isRegularFile(p)) {
                        scanFile(p);
                    }
                }
            });
        } catch (final IOException e) {
            // Ignore.
        }
    }

    private void scanFile(Path file) {
        try {
            String path = file.toAbsolutePath().normalize().toString();
            if (feedId != null) {
                if (!file.getFileName().toString().startsWith(feedId)) {
                    return;
                }
            }

            if (path.endsWith("meta.bgz")) {
                String bdyPath = path.substring(0, path.length() - 4) + ".bdy.dat";
                RANestedInputStream nestedInputStream = new RANestedInputStream(new BlockGZIPInputFile(file),
                        new UncompressedInputStream(Paths.get(bdyPath), true));
                int segment = 0;
                while (nestedInputStream.getNextEntry()) {
                    segment++;

                    MetaMap metaMap = new MetaMap();
                    metaMap.read(nestedInputStream, false);
                    nestedInputStream.closeEntry();

                    boolean match = true;

                    for (String matchKey : matchMap.keySet()) {
                        if (!metaMap.containsKey(matchKey)) {
                            // No Good
                            match = false;
                        } else {
                            if (!metaMap.get(matchKey).startsWith(matchMap.get(matchKey))) {
                                // No Good
                                match = false;
                            }
                        }
                    }

                    if (match) {
                        // Found Match
                        System.out.println("Found Match in " + path + " at segment " + segment);
                        System.out.write(metaMap.toByteArray());
                        System.out.println();
                    }

                }
                nestedInputStream.close();
            }
        } catch (IOException ioEx) {
            ioEx.printStackTrace();
        }
    }

    public boolean matches(String path) {
        String[] pathParts = path.split("/");

        for (int i = 0; (i < pathParts.length) && (i < repoPathParts.length); i++) {
            for (int c = 0; c < pathParts[i].length(); c++) {
                if (repoPathParts[i].charAt(c) == '*') {
                    break;
                }
                if (repoPathParts[i].charAt(c) != pathParts[i].charAt(c)) {
                    return false;
                }
            }
        }

        return true;
    }

}
