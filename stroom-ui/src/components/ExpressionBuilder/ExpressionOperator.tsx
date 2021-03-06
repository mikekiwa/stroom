/*
 * Copyright 2018 Crown Copyright
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import * as React from "react";
import { compose, withHandlers, withProps } from "recompose";
import { connect } from "react-redux";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import {
  DragSource,
  DropTarget,
  DropTargetCollector,
  DropTargetSpec,
  DragSourceSpec
} from "react-dnd";

import { canMove } from "../../lib/treeUtils";
import {
  DragDropTypes,
  DragObject,
  dragCollect,
  DragCollectedProps,
  DropCollectedProps
} from "./dragDropTypes";
import ExpressionTerm from "./ExpressionTerm";
import { actionCreators } from "./redux";
import Button from "../Button";
import {
  DataSourceType,
  ExpressionOperatorType,
  ExpressionTermType,
  OperatorType,
  ExpressionOperatorWithUuid,
  OperatorTypeValues,
  ExpressionItemWithUuid,
  ExpressionTermWithUuid
} from "../../types";
import { GlobalStoreState } from "../../startup/reducers";

const {
  expressionTermAdded,
  expressionOperatorAdded,
  expressionItemUpdated,
  expressionItemMoved,
  expressionItemDeleteRequested
} = actionCreators;

export interface Props {
  dataSource: DataSourceType;
  expressionId: string;
  operator: ExpressionOperatorWithUuid;
  isRoot?: boolean;
  isEnabled: boolean;
}

interface ConnectState {}
interface ConnectDispatch {
  expressionTermAdded: typeof expressionTermAdded;
  expressionOperatorAdded: typeof expressionOperatorAdded;
  expressionItemUpdated: typeof expressionItemUpdated;
  expressionItemMoved: typeof expressionItemMoved;
  expressionItemDeleteRequested: typeof expressionItemDeleteRequested;
}

export interface DndProps extends Props, ConnectState, ConnectDispatch {}

interface WithHandlers {
  onAddOperator: () => void;
  onAddTerm: () => void;
  onOperatorUpdated: (
    updates: ExpressionTermType | ExpressionOperatorType
  ) => void;
  onOpChange: (op: OperatorType) => void;
  onRequestDeleteOperator: () => void;
  onEnabledToggled: () => void;
}

interface WithProps {
  enabledColour: string;
  dndBarColour: string;
  className: string;
}
export interface EnhancedProps
  extends Props,
    ConnectState,
    ConnectDispatch,
    DragCollectedProps,
    DropCollectedProps,
    WithHandlers,
    WithProps {}

const dragSource: DragSourceSpec<DndProps, DragObject> = {
  canDrag(props) {
    return true;
  },
  beginDrag(props) {
    return {
      expressionItem: props.operator
    };
  }
};

const dropTarget: DropTargetSpec<Props & ConnectState & ConnectDispatch> = {
  canDrop(props, monitor) {
    return canMove(monitor.getItem(), props.operator);
  },
  drop(props, monitor) {
    props.expressionItemMoved(
      props.expressionId,
      monitor.getItem().expressionItem,
      props.operator
    );
  }
};

let dropCollect: DropTargetCollector<DropCollectedProps> = function dropCollect(
  connect,
  monitor
) {
  return {
    connectDropTarget: connect.dropTarget(),
    isOver: monitor.isOver(),
    canDrop: monitor.canDrop()
  };
};

const enhance = compose<EnhancedProps, Props>(
  connect<ConnectState, ConnectDispatch, Props, GlobalStoreState>(
    state => ({
      // operators are nested, so take all their props from parent
    }),
    {
      expressionTermAdded,
      expressionOperatorAdded,
      expressionItemUpdated,
      expressionItemMoved,
      expressionItemDeleteRequested
    }
  ),
  DragSource(DragDropTypes.OPERATOR, dragSource, dragCollect),
  DropTarget(
    [DragDropTypes.OPERATOR, DragDropTypes.TERM],
    dropTarget,
    dropCollect
  ),
  withHandlers<Props & ConnectState & ConnectDispatch, WithHandlers>({
    onAddOperator: ({
      expressionOperatorAdded,
      expressionId,
      operator: { uuid }
    }) => () => {
      expressionOperatorAdded(expressionId, uuid);
    },

    onAddTerm: ({
      expressionTermAdded,
      expressionId,
      operator: { uuid }
    }) => () => {
      expressionTermAdded(expressionId, uuid);
    },

    onOperatorUpdated: ({
      expressionItemUpdated,
      expressionId,
      operator: { uuid }
    }) => updates => {
      expressionItemUpdated(expressionId, uuid, updates);
    },

    onOpChange: ({
      expressionItemUpdated,
      expressionId,
      operator: { uuid }
    }) => op => {
      expressionItemUpdated(expressionId, uuid, {
        op
      });
    },

    onRequestDeleteOperator: ({
      expressionItemDeleteRequested,
      expressionId,
      operator: { uuid }
    }) => () => {
      expressionItemDeleteRequested(expressionId, uuid);
    },

    onEnabledToggled: ({
      isRoot,
      expressionItemUpdated,
      expressionId,
      operator: { uuid, enabled }
    }) => () => {
      if (!isRoot) {
        expressionItemUpdated(expressionId, uuid, {
          enabled: !enabled
        });
      }
    }
  }),
  withProps(({ canDrop, isOver, isRoot, isEnabled, operator }) => {
    let dndBarColour = "grey";
    if (isOver) {
      dndBarColour = canDrop ? "blue" : "red";
    }

    const classNames = ["expression-item"];
    if (isRoot) {
      classNames.push("expression-item__root");
    }
    if (!isEnabled) {
      classNames.push("expression-item--disabled");
    }

    let enabledColour = "grey";
    if (operator.enabled) {
      enabledColour = "blue";
    }

    return {
      enabledColour,
      dndBarColour,
      className: classNames.join(" ")
    };
  })
);

const ExpressionOperator = ({
  expressionId,
  operator,
  isRoot,
  isEnabled,
  dataSource,

  connectDropTarget,
  isOver,
  connectDragSource,

  dndBarColour,
  className,

  onAddOperator,
  onAddTerm,
  onOpChange,
  onRequestDeleteOperator,
  onEnabledToggled,

  enabledColour
}: EnhancedProps) => (
  <div className={className}>
    {connectDropTarget(
      <div>
        {connectDragSource(
          <span>
            <FontAwesomeIcon color={dndBarColour} icon="bars" />
          </span>
        )}

        {OperatorTypeValues.map((l, i) => (
          <Button
            selected={operator.op === l}
            key={l}
            groupPosition={
              i === 0
                ? "left"
                : OperatorTypeValues.length - 1 === i
                  ? "right"
                  : "middle"
            }
            onClick={() => onOpChange(l)}
            text={l}
          />
        ))}

        <div className="ExpressionItem__buttons">
          <Button
            icon="plus"
            text="Term"
            groupPosition="left"
            onClick={onAddTerm}
          />
          <Button
            icon="plus"
            text="Group"
            groupPosition={isRoot ? "right" : "middle"}
            onClick={onAddOperator}
          />
          {!isRoot && (
            <React.Fragment>
              <Button
                icon="check"
                groupPosition="middle"
                color={enabledColour}
                onClick={onEnabledToggled}
              />
              <Button
                icon="trash"
                groupPosition="right"
                onClick={onRequestDeleteOperator}
              />
            </React.Fragment>
          )}
        </div>
      </div>
    )}

    <div className="operator__children">
      {isOver &&
        dropTarget.canDrop && <div className="operator__placeholder" />}
      {operator.children &&
        operator.children
          .map((c: ExpressionItemWithUuid) => {
            let itemElement;
            switch (c.type) {
              case "term":
                itemElement = (
                  <div key={c.uuid}>
                    <ExpressionTerm
                      dataSource={dataSource}
                      expressionId={expressionId}
                      isEnabled={isEnabled && c.enabled}
                      term={c as ExpressionTermWithUuid}
                    />
                  </div>
                );
                break;
              case "operator":
                itemElement = (
                  <EnhancedExpressionOperator
                    dataSource={dataSource}
                    expressionId={expressionId}
                    isEnabled={isEnabled && c.enabled}
                    operator={c as ExpressionOperatorWithUuid}
                  />
                );
                break;
              default:
                throw new Error(`Invalid operator type: ${c.type}`);
            }

            // Wrap it with a line to
            return <div key={c.uuid}>{itemElement}</div>;
          })
          .filter(c => !!c) // null filter
      }
    </div>
  </div>
);

const EnhancedExpressionOperator = enhance(ExpressionOperator);

export default EnhancedExpressionOperator;
