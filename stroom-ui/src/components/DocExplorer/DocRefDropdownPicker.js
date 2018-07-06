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

import { compose } from 'recompose';
import { connect } from 'react-redux';

import { Select, Breadcrumb } from 'semantic-ui-react';

import { iterateNodes, findItem } from 'lib/treeUtils';

import { actionCreators } from './redux';

import withExplorerTree from './withExplorerTree';
import withDocRefTypes from './withDocRefTypes';

const { docRefPicked } = actionCreators;

const enhance = compose(
  withExplorerTree,
  withDocRefTypes,
  connect(
    (state, props) => ({
      documentTree: state.explorerTree.documentTree,
      docRef: state.docRefPicker[props.pickerId],
    }),
    {
      docRefPicked,
    },
  ),
);

const DocRefDropdownPicker = ({
  pickerId, documentTree, typeFilter, docRef, docRefPicked,
}) => {
  const value = docRef ? docRef.uuid : '';
  console.log({ pickerId, value });

  const options = [];
  iterateNodes(documentTree, (lineage, node) => {
    // If we are filtering on type, check this now
    if (!!typeFilter && typeFilter !== node.type) {
      return; // just skip out
    }

    // Compose the data that provides the breadcrumb to this node
    const sections = lineage.map(l => ({
      key: l.name,
      content: l.name,
      link: true,
    }));

    // Don't include folders as pickable items
    if (!node.children && node.uuid) {
      options.push({
        key: node.uuid,
        text: node.name,
        value: node.uuid,
        content: (
          <div style={{ width: '50rem' }}>
            <Breadcrumb size="mini" icon="right angle" sections={sections} />
            <div className="doc-ref-dropdown__item-name">{node.name}</div>
          </div>
        ),
      });
    }
  });

  const onDocRefSelected = (e, { value }) => {
    const picked = findItem(documentTree, value);
    docRefPicked(pickerId, picked);
  };

  return (
    <Select
      search
      options={options}
      value={value}
      onChange={onDocRefSelected}
      placeholder="Choose an option"
    />
  );
};

DocRefDropdownPicker.propTypes = {
  pickerId: PropTypes.string.isRequired,
  typeFilter: PropTypes.string,
};

export default enhance(DocRefDropdownPicker);
