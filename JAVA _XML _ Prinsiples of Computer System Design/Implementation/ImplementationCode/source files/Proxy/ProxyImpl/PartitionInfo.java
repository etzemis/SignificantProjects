package ProxyImpl;

import java.util.ArrayList;

import partitionimpl.AccountManagerPartition;

public class PartitionInfo {
		private AccountManagerPartition P;
		private boolean state;
		private ArrayList<Integer> BranchIDs = new ArrayList<Integer>();
		
		public AccountManagerPartition getP() {
			return P;
		}
		public void setP(AccountManagerPartition p) {
			P = p;
		}
		
		public boolean isState() {
			return state;
		}
		public void setState(boolean state) {
			this.state = state;
		}
		
		public void addBrId(int id){
			BranchIDs.add(new Integer(id));
		}
		
		public boolean hasBr(int id){
			return BranchIDs.contains(id);
		}
		

}
