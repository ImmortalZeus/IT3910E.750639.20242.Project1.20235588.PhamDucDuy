package models.utils;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.PosixFileAttributes;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.UserPrincipal;
import java.nio.file.attribute.UserPrincipalLookupService;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Set;

public class isSecureFile {
    public static final boolean check(String filepath) {
        try {
            return isSecureFile.check(Paths.get(filepath).toRealPath());
        } catch (Exception e) {
            return false;
        }
    }

    public static final boolean check(Path file) {
        return isSecureFile.check(file, null);
    }

    public static final boolean check(String filepath, UserPrincipal user) {
        try {
            return isSecureFile.check(Paths.get(filepath).toRealPath(), user);
        } catch (Exception e) {
            return false;
        }
    }
    
    public static final boolean check(Path file, UserPrincipal user) {
        return isSecureFile.check(file, user, 1);
    }

    public static final boolean check(String filepath, UserPrincipal user, int symlinkDepth) {
        try {
            return isSecureFile.check(Paths.get(filepath).toRealPath(), user, symlinkDepth);
        } catch (Exception e) {
            return false;
        }
    }
    
    public static final boolean check(Path file, UserPrincipal user, int symlinkDepth) {
        if(file == null) {
            return false;
        }

        try {
            file = file.toRealPath();
        } catch (IOException x) {
            return false;
        }

        if (symlinkDepth <= 0) {
            // Too many levels of symbolic links
            return false;
        }

        // Get UserPrincipal for specified user and superuser
        FileSystem fileSystem = Paths.get(file.getRoot().toString()).getFileSystem();
        UserPrincipalLookupService upls = fileSystem.getUserPrincipalLookupService();
        UserPrincipal root = null;
        try {
            root = upls.lookupPrincipalByName("root");
            if (user == null) {
                user = upls.lookupPrincipalByName(System.getProperty("user.name"));
            }
            if (root == null || user == null) {
                return false;
            }
        } catch (IOException x) {
            return false;
        }
        // If any parent dirs (from root on down) are not secure,
        // dir is not secure
        for (int i = 1; i <= file.getNameCount(); i++) {
            Path partialPath = Paths.get(file.getRoot().toString(),
                file.subpath(0, i).toString());

            try {
                if (Files.isSymbolicLink(partialPath)) {
                    if (!isSecureFile.check(Files.readSymbolicLink(partialPath), user, symlinkDepth - 1)) {
                        // Symbolic link, linked-to dir not secure
                        return false;
                    }
                } else {
                    UserPrincipal owner = Files.getOwner(partialPath);
                    if (!user.equals(owner) && !root.equals(owner)) {
                        // dir owned by someone else, not secure
                        return false;
                    }
                    PosixFileAttributes attr =
                        Files.readAttributes(partialPath, PosixFileAttributes.class);
                    Set < PosixFilePermission > perms = attr.permissions();
                    if (perms.contains(PosixFilePermission.GROUP_WRITE) ||
                        perms.contains(PosixFilePermission.OTHERS_WRITE)) {
                        // Someone else can write files, not secure
                        return false;
                    }
                }
            } catch (IOException x) {
                return false;
            }
        }

        try {
            BasicFileAttributes attr = Files.readAttributes(file, BasicFileAttributes.class, LinkOption.NOFOLLOW_LINKS);
            
            // Check
            if (!attr.isRegularFile()) {
                return false;
            }
        } catch (IOException x) {
            return false;
        }

        return true;
    }
}