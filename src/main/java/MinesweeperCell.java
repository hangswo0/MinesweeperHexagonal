class MinesweeperCell {
    boolean isOpen, isFlag, isMine;

    void open() {
        if (!isFlag)
            isOpen = true;
    }

    void setEmpty() {
        isMine = false;
        isOpen = false;
    }

    void setFlag() {
        isOpen = false;
        isFlag = true;
    }

    void removeFlag() {
        isOpen = false;
        isFlag = false;
    }

    void setMined() {
        isMine = true;
    }

    boolean opened() {
        return isOpen;
    }

    boolean closed() {
        return !isOpen;
    }

    boolean mined() {
        return isMine;
    }

    boolean empty() {
        return !isMine;
    }

    boolean flagged() {
        return isFlag;
    }

    boolean notFlagged() {
        return !isFlag;
    }
}