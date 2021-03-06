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
import { connect } from "react-redux";
import { compose, withHandlers } from "recompose";

import IconHeader from "../../components/IconHeader";
import { actionCreators } from "./redux";
import { GlobalStoreState } from "../../startup/reducers";

const { themeChanged } = actionCreators;

const themeOptions = [
  {
    text: "Light",
    value: "theme-light"
  },
  {
    text: "Dark",
    value: "theme-dark"
  }
];

export interface Props {}

interface ConnectState {
  theme: string;
}

interface ConnectDispatch {
  themeChanged: typeof themeChanged;
}

interface Handlers {
  onThemeChanged: React.ChangeEventHandler<HTMLSelectElement>;
}

export interface EnhancedProps
  extends Props,
    ConnectState,
    ConnectDispatch,
    Handlers {}

const enhance = compose<EnhancedProps, Props>(
  connect<ConnectState, ConnectDispatch, Props, GlobalStoreState>(
    ({ userSettings: { theme } }) => ({
      theme
    }),
    { themeChanged }
  ),
  withHandlers<Props & ConnectState & ConnectDispatch, Handlers>({
    onThemeChanged: ({ themeChanged }) => ({ target: { value } }) => {
      themeChanged(value);
    }
  })
);

const UserSettings = ({ theme, onThemeChanged }: EnhancedProps) => (
  <div className="UserSettings">
    <IconHeader text="User Settings" icon="user" />
    <div className="UserSettings__container">
      <div>
        <label>Theme:</label>
        <select onChange={onThemeChanged} value={theme}>
          {themeOptions.map(theme => (
            <option key={theme.value} value={theme.value}>
              {theme.text}
            </option>
          ))}
        </select>
      </div>
    </div>
  </div>
);

export default enhance(UserSettings);
