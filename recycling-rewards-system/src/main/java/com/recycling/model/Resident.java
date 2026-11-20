package com.recycling.model;

public class Resident {

    private int residentId;
    private String name;
    private String membershipId;
    private String phone;
    private int rewardBalance;

    public Resident(int residentId, String name, String membershipId,
                  String phone, int rewardBalance) {

        this.residentId = residentId;
        this.name = name;
        this.membershipId = membershipId;
        this.phone = phone;
        this.rewardBalance = rewardBalance;
    }

    

    public int getResidentId() {
        return residentId;
    }

    public String getName() {
        return name;
    }

    public String getMembershipId() {
        return membershipId;
    }

    public String getPhone() {
        return phone;
    }

    public int getRewardBalance() {
        return rewardBalance;
    }

   

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setRewardBalance(int rewardBalance) {
        this.rewardBalance = rewardBalance;
    }

    // Reward point operations

    public void addPoints(int points) {
        rewardBalance += points;
    }

    public void removePoints(int points) {
        rewardBalance -= points;
    }
}