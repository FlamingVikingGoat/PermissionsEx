package ru.tehkode.permissions;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author code
 */
public abstract class PermissionUser extends PermissionNode {

    public PermissionUser(String playerName, PermissionManager manager) {
        super(playerName, manager);
    }

    public boolean inGroup(PermissionGroup group) {
        return this.inGroup(group.getName());
    }

    public boolean inGroup(String groupName) {
        for (String matchingGroupName : this.getGroupNames()) {
            if (groupName.equalsIgnoreCase(matchingGroupName)) {
                return true;
            }
        }

        return false;
    }

    public PermissionGroup[] getGroups() {
        Set<PermissionGroup> groups = new LinkedHashSet<PermissionGroup>();

        for (String group : this.getGroupNames()) {
            groups.add(this.manager.getGroup(group.trim()));
        }

        return groups.toArray(new PermissionGroup[]{});
    }

    @Override
    public boolean has(String permission, String world) {
        String expression = this.getMatchingExpression(permission, world);
        if (expression != null) {
            return this.explainExpression(expression);
        }

        for (PermissionGroup group : this.getGroups()) {
            if (group.has(permission, world)) {
                return true;
            }
        }

        return false;
    }

    public void addGroup(String groupName) {
        if (groupName == null || groupName.isEmpty()) {
            return;
        }

        this.addGroup(this.manager.getGroup(groupName));
    }

    public void addGroup(PermissionGroup group) {
        if (group == null) {
            return;
        }

        List<PermissionGroup> groups = Arrays.asList(this.getGroups());

        if (!groups.contains(group)) {
            groups.add(group);

            this.setGroups(groups.toArray(new PermissionGroup[]{}));
        }
    }

    public void removeGroup(String groupName) {
        if (groupName == null || groupName.isEmpty()) {
            return;
        }

        this.removeGroup(this.manager.getGroup(groupName));
    }

    public void removeGroup(PermissionGroup group) {
        if (group == null) {
            return;
        }

        List<PermissionGroup> groups = Arrays.asList(this.getGroups());

        if (groups.contains(group)) {
            groups.remove(group);

            this.setGroups(groups.toArray(new PermissionGroup[]{}));
        }
    }

    public abstract void setGroups(PermissionGroup[] groups);

    /**
     * @todo: Think about moving this to protected
     */
    protected abstract String[] getGroupNames();
}
