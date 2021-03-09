package gr.ste.domain.entities;

import gr.ste.presentation.events.MoveEnteredEvent;
import javafx.geometry.Orientation;

import java.util.*;

public class NPCPlayer extends Player {
    private final Map<Integer, EnemyShipInformation> enemyShipInformationMap;

    public NPCPlayer(int id, String name, Board board) {
        super(id, name, board, PlayerType.npc);
        this.enemyShipInformationMap = new HashMap<>();
    }

    @Override
    public void update(Move move) {
        super.update(move);
        if(move.getHitShip() != null) {
            EnemyShipInformation shipInformation = enemyShipInformationMap.get(move.getTargetId());
            if (shipInformation != null) {
                shipInformation.addShipLocation(move);
            } else {
                enemyShipInformationMap.put(move.getTargetId(), new EnemyShipInformation(move));
            }

            if(move.getHitShip().isSunk()) {
                enemyShipInformationMap.put(move.getTargetId(), null);
            }
        }
    }

    public EnemyShipInformation getEnemyShipInformation(int enemyId) {
        return enemyShipInformationMap.get(enemyId);
    }

    public MoveEnteredEvent chooseMove(List<Player> players) {
        int enemyId = selectRandomEnemy(players);
        Position target = selectTargetPosition(enemyId);
        return new MoveEnteredEvent(target, enemyId);
    }

    public int selectRandomEnemy(List<Player> players) {
        Random rand = new Random();
        int targetId = rand.nextInt(players.size());
        while(targetId == this.getId()) {
            targetId = rand.nextInt(players.size());
        }
        return targetId;
    }

    public Position selectTargetPosition(int enemyId) {
        Random rand = new Random();
        Position targetPosition;

        Stack<Move> pastMoves = this.getPastMoves(enemyId);
        List<Position> availableMoves = this.getAvailableMoves(enemyId);
        EnemyShipInformation enemyShipInformation = this.getEnemyShipInformation(enemyId);
        if(enemyShipInformation != null) {
            List<Position> possibleMoves = new ArrayList<>();
            List<Position> knownShipLocations = enemyShipInformation.getShipLocations();

            if(enemyShipInformation.isOrientationDiscovered().isPresent()) {
                Orientation knownOrientation = enemyShipInformation.isOrientationDiscovered().get();
                List<Position> extremePositions = enemyShipInformation.getExtremeLocations();
                Position minPosition = extremePositions.get(0);
                Position maxPosition = extremePositions.get(extremePositions.size() - 1);
                if(knownOrientation == Orientation.HORIZONTAL) {
                    possibleMoves.add(minPosition.add(new Position(-1,0)));
                    possibleMoves.add(maxPosition.add(new Position(1,0)));
                } else {
                    possibleMoves.add(minPosition.add(new Position(0,-1)));
                    possibleMoves.add(maxPosition.add(new Position(0,1)));
                }
            } else {
                Position knowShipLocation = knownShipLocations.get(0);
                possibleMoves.add(knowShipLocation.add(new Position(-1,0)));
                possibleMoves.add(knowShipLocation.add(new Position(1,0)));
                possibleMoves.add(knowShipLocation.add(new Position(0,-1)));
                possibleMoves.add(knowShipLocation.add(new Position(0,1)));
            }

            possibleMoves.removeIf(pastMoves::contains);
            possibleMoves.removeIf(s -> !getBoard().isInside(s));
            if(possibleMoves.isEmpty()) {
                int randomIndex = rand.nextInt(availableMoves.size());
                targetPosition = availableMoves.get(randomIndex);
            } else {
                int randomIndex = rand.nextInt(possibleMoves.size());
                targetPosition = possibleMoves.get(randomIndex);
            }
        } else {
            int randomIndex = rand.nextInt(availableMoves.size());
            targetPosition = availableMoves.get(randomIndex);
        }
        return targetPosition;
    }
}