package inc.elevati.imycity.utils;

public interface MvpContracts {

    interface MvpView {}

    interface MvpPresenter {

        void attachView(MvpView view);

        void detachView();
    }
}
