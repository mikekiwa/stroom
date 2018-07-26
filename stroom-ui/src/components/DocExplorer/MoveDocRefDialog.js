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
import React from 'react';
import PropTypes from 'prop-types';

import { compose, withProps } from 'recompose';
import { connect } from 'react-redux';

import { Modal, Button } from 'semantic-ui-react';

import { guid } from 'lib/treeUtils';
import { actionCreators } from './redux';
import { moveDocuments } from './explorerClient';

import DocPicker from './DocPicker/DocPicker';
import PermissionInheritancePicker from 'components/PermissionInheritancePicker';

const { completeDocRefMove } = actionCreators;

const enhance = compose(
  withProps(({ explorerId }) => ({
    explorerId: `move-doc-ref-${explorerId || guid()}`,
  })),
  connect(
    ({ docExplorer, permissionInheritancePicker }, { explorerId }) => {
      let selectedDocRef;
      const explorer = docExplorer.explorerTree.explorers[explorerId];
      if (explorer) {
        const s = Object.entries(explorer.isSelected)
          .filter(k => k[1])
          .map(k => ({ uuid: k[0] }));
        if (s.length > 0) {
          selectedDocRef = s[0];
        }
      }

      return {
        isMoving: docExplorer.moveDocRef.isMoving,
        docRefs: docExplorer.moveDocRef.docRefs,
        permissionInheritance: permissionInheritancePicker[explorerId],
        selectedDocRef,
      };
    },
    { completeDocRefMove, moveDocuments },
  ),
);

const MoveDocRefDialog = ({
  explorerId,
  isMoving,
  docRefs,
  completeDocRefMove,
  moveDocuments,
  selectedDocRef,
  permissionInheritance,
}) => (
  <Modal open={isMoving}>
    <Modal.Header>Select a Destination Folder for the Move</Modal.Header>
    <Modal.Content scrolling>
      <DocPicker explorerId={explorerId} typeFilters={['Folder']} />
      <PermissionInheritancePicker pickerId={explorerId} />
    </Modal.Content>
    <Modal.Actions>
      <Button negative onClick={completeDocRefMove}>
        Cancel
      </Button>
      <Button
        positive
        onClick={() => moveDocuments(docRefs, selectedDocRef, permissionInheritance)}
        labelPosition="right"
        icon="checkmark"
        content="Choose"
      />
    </Modal.Actions>
  </Modal>
);

MoveDocRefDialog.propTypes = {
  explorerId: PropTypes.string.isRequired,
};

export default enhance(MoveDocRefDialog);
