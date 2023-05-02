
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

		@Override
		boolean isWhite() {
			return true;
		}

		@Override
		boolean isBlack() {
			return false;
		}

		@Override
		boolean getShortCastle() {
			return ChessBoard.whiteCanShortCastle;
		}

		@Override
		boolean getLongCastle() {
			return ChessBoard.whiteCanLongCastle;
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

		@Override
		boolean isWhite() {
			return false;
		}

		@Override
		boolean isBlack() {
			return true;
		}

		@Override
		boolean getShortCastle() {
			return ChessBoard.blackCanShortCastle;
		}

		@Override
		boolean getLongCastle() {
			return ChessBoard.blackCanLongCastle;
		}
	};

	abstract boolean isWhite();

	abstract boolean isBlack();

	abstract int getDirection();

	abstract int getStartingRank();

	abstract Alliance getOppositeSide();

	abstract boolean getShortCastle();

	abstract boolean getLongCastle();
}
