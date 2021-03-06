import { Action } from "redux";

import { prepareReducer } from "../../../lib/redux-actions-ts";
import {
  PipelineSearchResultType,
  PipelineSearchCriteriaType
} from "../../../types";

export interface StoreState {
  results: PipelineSearchResultType;
  criteria: PipelineSearchCriteriaType;
}

export const PIPELINES_RECEIVED = "PIPELINES_RECEIVED";
export const UPDATE_CRITERIA = "UPDATE_CRITERIA";

export interface PipelinesReceivedAction extends Action<"PIPELINES_RECEIVED"> {
  results: PipelineSearchResultType;
}

export interface UpdateCriteriaAction extends Action<"UPDATE_CRITERIA"> {
  criteria: PipelineSearchCriteriaType;
}

const defaultState: StoreState = {
  results: {
    total: 0,
    pipelines: []
  },
  criteria: {
    filter: "",
    pageOffset: 0,
    pageSize: 10
  }
};

export const actionCreators = {
  pipelinesReceived: (
    results: PipelineSearchResultType
  ): PipelinesReceivedAction => ({
    type: PIPELINES_RECEIVED,
    results
  }),
  updateCriteria: (
    criteria: PipelineSearchCriteriaType
  ): UpdateCriteriaAction => ({
    type: UPDATE_CRITERIA,
    criteria
  })
};

export const reducer = prepareReducer(defaultState)
  .handleAction<PipelinesReceivedAction>(
    PIPELINES_RECEIVED,
    (state = defaultState, { results }) => ({
      ...state,
      results
    })
  )
  .handleAction<UpdateCriteriaAction>(
    UPDATE_CRITERIA,
    (state = defaultState, { criteria }) => ({
      ...state,
      criteria
    })
  )
  .getReducer();
