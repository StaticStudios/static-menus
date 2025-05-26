package net.staticstudios.menus.menu;

public class MenuBuilderSelector {

    protected MenuBuilderSelector() {
    }

    /**
     * Get a {@link SimpleMenu} builder
     *
     * @return The builder
     */
    public SimpleMenuBuilder simple() {
        return new SimpleMenuBuilder(false);
    }

    /**
     * Get a {@link TemplatedMenu} builder
     *
     * @param template The template
     * @return The builder
     */
    public TemplatedMenuBuilder templated(String template) {
        return new TemplatedMenuBuilder(false, template);
    }

    /**
     * Get a {@link PagedMenu} builder
     *
     * @param template The template
     * @return The builder
     */
    public PagedMenuBuilder paged(String template) {
        return new PagedMenuBuilder(false, template);
    }

    /**
     * Get a {@link PagedMenu} builder
     *
     * @param size The size
     * @return The builder
     */
    public PagedMenuBuilder paged(int size) {
        return new PagedMenuBuilder(false, "?".repeat(size)).marker('?');
    }

    /**
     * Get a mutable {@link SimpleMenu} builder.
     *
     * @return The builder
     */
    public SimpleMenuBuilder mutableSimple() {
        return new SimpleMenuBuilder(true);
    }

    /**
     * Get a mutable {@link TemplatedMenu} builder.
     *
     * @param template The template
     * @return The builder
     */
    public TemplatedMenuBuilder mutableTemplated(String template) {
        return new TemplatedMenuBuilder(true, template);
    }

    /**
     * Get a mutable {@link PagedMenu} builder.
     *
     * @param template The template
     * @return The builder
     */
    public PagedMenuBuilder mutablePaged(String template) {
        return new PagedMenuBuilder(true, template);
    }

    /**
     * Get a mutable {@link InteractableMenuBuilder}.
     *
     * @param template The template for the menu.
     * @return The builder for the interactable menu.
     */
    public InteractableMenuBuilder interactable(String template) {
        return new InteractableMenuBuilder(false, template);
    }

    /**
     * Get a mutable {@link InteractableMenuBuilder}.
     *
     * @param template The template for the menu.
     * @return The builder for the mutable interactable menu.
     */
    public InteractableMenuBuilder mutableInteractable(String template) {
        return new InteractableMenuBuilder(true, template);
    }
}
