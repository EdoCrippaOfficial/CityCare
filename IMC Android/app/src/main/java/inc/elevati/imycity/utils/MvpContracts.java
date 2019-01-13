package inc.elevati.imycity.utils;

/** Generic interface that defines the main interfaces for View and Presenter (MVP architecture) */
public interface MvpContracts {

    /**
     * The View interface
     */
    interface MvpView {}

    /**
     * The presenter interface
     */
    interface MvpPresenter {

        /**
         * Attaches a view to this presenter
         * @param view the view to attach
         */
        void attachView(MvpView view);

        /**
         * Detaches the view from the presenter, if one was attached
         */
        void detachView();
    }
}
