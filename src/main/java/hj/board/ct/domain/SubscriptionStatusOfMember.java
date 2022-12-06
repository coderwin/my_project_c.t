package hj.board.ct.domain;

/**
 * 회원 가입 상태 표시
 */
public enum SubscriptionStatusOfMember {

    J("join"),
    W("withdrawal");

    private final String subscriptionStatus; // 가입현황

    SubscriptionStatusOfMember(String subscriptionStatus) {
        this.subscriptionStatus = subscriptionStatus;
    }

    public String getSubscriptionStatus() {
        return subscriptionStatus;
    }



}
