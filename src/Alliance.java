
public enum Alliance {
	WHITE {
		@Override
		int getDirection() {
			return 1;
		}

		@Override
		int getStartingRank() {
			return 1;
		}

		@Override
		Alliance getOppositeSide() {
			return Alliance.BLACK;
		}
	},
	BLACK {
		@Override
		int getDirection() {
			return -1;
		}

		@Override
		int getStartingRank() {
			return 6;
		}

		@Override
		Alliance getOppositeSide() {
			return Alliance.WHITE;
		}
	};

	abstract int getDirection();

	abstract int getStartingRank();

	abstract Alliance getOppositeSide();
}
